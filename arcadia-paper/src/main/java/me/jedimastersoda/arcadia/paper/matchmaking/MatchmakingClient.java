package me.jedimastersoda.arcadia.paper.matchmaking;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.jedimastersoda.arcadia.common.db.RedisConnection;
import me.jedimastersoda.arcadia.common.highway.HighwayListener;
import me.jedimastersoda.arcadia.common.highway.RedisServerHighway;
import me.jedimastersoda.arcadia.common.object.ServerStatus;
import redis.clients.jedis.Jedis;

public class MatchmakingClient {

  private static MatchmakingClient instance;

  public static MatchmakingClient getInstance() {
    return instance;
  }

  public static void init(JavaPlugin javaPlugin) {
    if(instance == null) {
      instance = new MatchmakingClient(javaPlugin);
    }
  }

  private final JavaPlugin javaPlugin;
  private Map<Player, MatchmakingLobby> matchmakingLobbyMap = new HashMap<>();

  private MatchmakingClient(JavaPlugin javaPlugin) {
    this.javaPlugin = javaPlugin;

    RedisServerHighway.getInstance().registerListener(new HighwayListener() {
      @Override
      public void onMessage(String[] messageFragments) {
        if(messageFragments[0].equals("2")) {
          UUID uuid = UUID.fromString(messageFragments[1]);
          Player player = Bukkit.getPlayer(uuid);

          if(player != null) {
            MatchmakingLobby matchmakingLobby = matchmakingLobbyMap.get(player);

            if(matchmakingLobby != null) {
              matchmakingLobby.setPlayersLeft(Integer.parseInt(messageFragments[2]));
              matchmakingLobby.setEstimatedWaitTime(Integer.parseInt(messageFragments[3]));
            }
          }
        }
      }
      
      @Override
      public void onServerUpdate(ServerStatus serverStatus) {}
    });

    // TO MATCHMAKER: 1|uuid|init/cancel|game_rr
    // TO SERVER: 2|uuid|playersLeft|estWaitTimeSeconds
  }

  public boolean enterMatchmaking(Player player, String game) {
    if(!matchmakingLobbyMap.containsKey(player)) {
      MatchmakingLobby matchmakingLobby = new MatchmakingLobby(-1, -1);
      matchmakingLobbyMap.put(player, matchmakingLobby);
      
      Jedis jedis = RedisConnection.getInstance().getRedisConnection();
      jedis.publish("highway", "1|" + player.getUniqueId() + "|init|" + game);
      jedis.close();

      new BukkitRunnable() {
        @Override
        public void run() {
          if(player.isOnline() && matchmakingLobbyMap.containsKey(player)) {
            player.sendActionBar(matchmakingLobby.getActionBarMessage());
          } else {
            this.cancel();

            leaveMatchmaking(player);
          }
        }
      }.runTaskTimer(this.javaPlugin, 0, 10L);

      return true;
    } else {
      return false;
    }
  }

  public boolean leaveMatchmaking(Player player) {
    if(matchmakingLobbyMap.containsKey(player)) {
      matchmakingLobbyMap.remove(player);

      Jedis jedis = RedisConnection.getInstance().getRedisConnection();
      jedis.publish("highway", "1|" + player.getUniqueId() + "|cancel");
      jedis.close();

      return true;
    } else {
      return false;
    }
  }
}