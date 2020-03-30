package me.jedimastersoda.arcadia.velocity.matchmaking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import me.jedimastersoda.arcadia.common.db.RedisConnection;
import me.jedimastersoda.arcadia.common.highway.HighwayListener;
import me.jedimastersoda.arcadia.common.highway.RedisServerHighway;
import me.jedimastersoda.arcadia.common.object.ServerStatus;
import me.jedimastersoda.arcadia.velocity.Main;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import redis.clients.jedis.Jedis;

public class MatchmakingController {

  private static MatchmakingController instance;

  public static MatchmakingController getInstance() {
    return instance;
  }

  public static void init(Main main) {
    if(instance == null) {
      instance = new MatchmakingController(main);
    }
  }

  private final Main main;

  private final Map<String, List<UUID>> playerServerQueue = new HashMap<>();
  private final Map<UUID, String> playerServerQueueIndex = new HashMap<>();
  private final Map<String, List<RegisteredServer>> serverMatchmakingQueue = new HashMap<>();
  private final Map<UUID, RegisteredServer> playerServerIndex = new HashMap<>();
  private final Map<RegisteredServer, MatchmakingLobby> matchmakingLobbies = new HashMap<>();

  // Queue checks serverMatchmakingQueue
  // If server not available, put in playerServerQueue
  // Put player in matchmakinglobby
  // send: 2|uuid|playersLeft|-1

  private MatchmakingController(Main main) {
    this.main = main;

    main.getProxyServer().getEventManager().register(main, this);

    RedisServerHighway.getInstance().registerListener(new HighwayListener() {
      @Override
      public void onMessage(String[] messageFragments) {
        if(messageFragments[0].equals("1")) {
          UUID uuid = UUID.fromString(messageFragments[1]);
          String action = messageFragments[2];

          if(action.equals("init")) {
            String game = messageFragments[3];

            enterQueue(uuid, game);
          } else {
            removeFromQueue(uuid);
          }
        }
      }

      @Override
      public void onServerUpdate(ServerStatus serverStatus) {
        if(serverStatus.getServerStatusMessage().equals("match_announce")) {
          Optional<RegisteredServer> optional = main.getProxyServer().getServer(serverStatus.getServerName());

          if(optional.isPresent()) {
            RegisteredServer registeredServer = optional.get();
            publishGameServer(registeredServer, serverStatus.getServerType(), serverStatus.getMaxPlayerCount());
          }
        } else if(serverStatus.getServerStatusMessage().equals("shutdown") && serverStatus.getServerType().startsWith("game_")) {
          Optional<RegisteredServer> optional = main.getProxyServer().getServer(serverStatus.getServerName());

          if(optional.isPresent()) {
            RegisteredServer registeredServer = optional.get();

            removeGameServer(registeredServer);
          }
        }
      }
    });

    // TO MATCHMAKER: 1|uuid|init/cancel|game_rr
    // TO SERVER: 2|uuid|playersLeft|estWaitTimeSeconds
  }

  public void publishGameServer(RegisteredServer registeredServer, String gameType, int playersNeededFull) {
    if(serverMatchmakingQueue.containsKey(gameType)) {
      serverMatchmakingQueue.get(gameType).add(registeredServer);
    } else {
      serverMatchmakingQueue.put(gameType, new ArrayList<RegisteredServer>(Arrays.asList(registeredServer)));
    }

    MatchmakingLobby matchmakingLobby = new MatchmakingLobby(main, registeredServer, gameType, playersNeededFull, this);
    matchmakingLobbies.put(registeredServer, matchmakingLobby);

    // Check playerServerQueue

    if(playerServerQueue.containsKey(gameType)) {
      List<UUID> playersInQueue = playerServerQueue.get(gameType);
      List<UUID> playersRemoved = new ArrayList<>();

      for(int i = 0; i < playersNeededFull; i++) {
        if(i < playersInQueue.size()) {
          UUID player = playersInQueue.get(i);

          playerServerIndex.put(player, matchmakingLobby.getServer());
          matchmakingLobby.addPlayer(player);

          playersRemoved.add(player);
          playerServerQueueIndex.remove(player);
        } else {
          break;
        }
      }

      playersRemoved.forEach(player -> playerServerQueue.get(gameType).remove(player));
    }
  }

  public void removeGameServer(RegisteredServer registeredServer) {
    if(!matchmakingLobbies.containsKey(registeredServer)) return;

    MatchmakingLobby matchmakingLobby = matchmakingLobbies.get(registeredServer);
    String gameType = matchmakingLobby.getGameType();
    serverMatchmakingQueue.get(gameType).remove(registeredServer);

    matchmakingLobbies.remove(registeredServer);

    matchmakingLobby.setServerRemoved(true);

    if(!matchmakingLobby.isStarted()) {
      matchmakingLobby.getPlayers().forEach(player -> {
        Optional<Player> optional = main.getProxyServer().getPlayer(player);
  
        if(optional.isPresent()) {
          Player velocityPlayer = optional.get();
  
          if(velocityPlayer.isActive()) {
            velocityPlayer.sendMessage(TextComponent.builder()
              .content("The game server has shutdown due to an unknown error.")
              .color(TextColor.RED)
              .build()
            );

            velocityPlayer.sendMessage(TextComponent.builder()
              .content("Finding another server instance...")
              .color(TextColor.BLUE)
              .build()
            );

            Jedis jedis = RedisConnection.getInstance().getRedisConnection();

            jedis.publish("highway", "2|" + velocityPlayer.getUniqueId().toString() + "|-1|-1");

            jedis.close();

            removeFromQueue(player);
            enterQueue(player, matchmakingLobby.getGameType());
          }
        }
      });
    }
  }

  public MatchmakingLobby findBestLobby(String game) {
    if(serverMatchmakingQueue.containsKey(game)) {
      List<RegisteredServer> possibleServers = serverMatchmakingQueue.get(game);

      for(RegisteredServer possibleServer : possibleServers) {
        MatchmakingLobby matchmakingLobby = matchmakingLobbies.get(possibleServer);

        if(matchmakingLobby.hasSlotAvailable()) {
          return matchmakingLobby;
        }
      }

      return null;
    } else {
      return null;
    }
  }

  public void enterQueue(UUID uuid, String game) {
    if(!playerServerIndex.containsKey(uuid) && !playerServerQueueIndex.containsKey(uuid)) {
      MatchmakingLobby matchmakingLobby = findBestLobby(game);

      if(matchmakingLobby != null) {
        playerServerIndex.put(uuid, matchmakingLobby.getServer());
        matchmakingLobby.addPlayer(uuid);
      } else {
        // Put player in queue to find a server matchmaking lobby
        
        playerServerQueueIndex.put(uuid, game);

        if(playerServerQueue.containsKey(game)) {
          playerServerQueue.get(game).add(uuid);
        } else {
          playerServerQueue.put(game, new ArrayList<UUID>(Arrays.asList(uuid)));
        }
      }
    }
  }

  public void removeFromQueue(UUID uuid) {
    if(playerServerQueueIndex.containsKey(uuid)) {
      playerServerQueue.get(playerServerQueueIndex.get(uuid)).remove(uuid);
      playerServerQueueIndex.remove(uuid);
    }

    if(playerServerIndex.containsKey(uuid)) {
      RegisteredServer server = playerServerIndex.get(uuid);

      playerServerIndex.remove(uuid);
      
      if(matchmakingLobbies.containsKey(server)) {
        matchmakingLobbies.get(server).removePlayer(uuid);
      }
    }
  }

  @Subscribe
  public void onPlayerQuit(DisconnectEvent event) {
    this.removeFromQueue(event.getPlayer().getUniqueId());
  }

  @Subscribe
  public void onPlayerChangeServer(ServerConnectedEvent event) {
    this.removeFromQueue(event.getPlayer().getUniqueId());
  }
}