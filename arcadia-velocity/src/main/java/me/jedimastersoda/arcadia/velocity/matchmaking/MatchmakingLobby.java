package me.jedimastersoda.arcadia.velocity.matchmaking;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.scheduler.ScheduledTask;

import lombok.Getter;
import lombok.Setter;
import me.jedimastersoda.arcadia.common.db.RedisConnection;
import me.jedimastersoda.arcadia.velocity.Main;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import redis.clients.jedis.Jedis;

public class MatchmakingLobby {

  private final Main main;
  @Getter final String gameType;
  private final MatchmakingController matchmakingController;
  @Getter final RegisteredServer server;

  @Getter final List<UUID> players = new ArrayList<>();
  @Getter private final int playersNeeded;
  @Getter private final int playersNeededFull;

  private boolean starting = false;
  @Getter boolean started = false;
  private ScheduledTask scheduledTask;

  @Getter @Setter private boolean serverRemoved = false;

  public MatchmakingLobby(Main main, RegisteredServer server, String gameType, int playersNeededFull, MatchmakingController matchmakingController) {
    this.main = main;
    this.gameType = gameType;
    this.matchmakingController = matchmakingController;
    this.server = server;

    this.playersNeededFull = playersNeededFull;
    this.playersNeeded = (this.playersNeededFull - 2) <= 0 ? 1 : this.playersNeededFull - 2;
  }

  public int getPlayersLeftToStart() {
    return this.playersNeeded - this.players.size();
  }

  public int getPlayersLeft() {
    return this.playersNeededFull - this.players.size();
  }

  public boolean hasSlotAvailable() {
    if(this.started) return false;

    return this.players.size() < this.playersNeededFull;
  }

  public void addPlayer(final UUID uuid) {
    this.players.add(uuid);

    Jedis jedis = RedisConnection.getInstance().getRedisConnection();

    this.players.forEach(player -> {
      jedis.publish("highway", "2|" + player.toString() 
        + "|" + this.getPlayersLeft() + "|" + this.getEstimatedWaitTime());
    });

    jedis.close();

    if(this.getPlayersLeftToStart() <= 0) {
      this.startMatch();
    }
  }

  private void startMatch() {
    if(this.starting) return;

    this.starting = true;

    this.scheduledTask = main.getProxyServer().getScheduler().buildTask(this.main, new Runnable() {
      int countdown = 15;

      @Override
      public void run() {
        if(serverRemoved) {
          scheduledTask.cancel();

          return;
        }

        if(getPlayersNeededFull() <= 0) {
          scheduledTask.cancel();

          sendPlayers();

          players.forEach(player -> matchmakingController.removeFromQueue(player));
        } else {
          if(getPlayersLeftToStart() <= 0) {
            if(countdown <= 1) {
              scheduledTask.cancel();

              sendPlayers();

              players.forEach(player -> matchmakingController.removeFromQueue(player));
            } else {
              countdown--;
            }
          } else {
            scheduledTask.cancel();
            starting = false;
          }
        }
      }
    }).repeat(1, TimeUnit.SECONDS).schedule();
  }

  private void sendPlayers() {
    this.started = true;

    matchmakingController.removeGameServer(server);

    players.forEach(player -> {
      Optional<Player> optional = main.getProxyServer().getPlayer(player);

      if(optional.isPresent()) {
        Player velocityPlayer = optional.get();

        velocityPlayer.createConnectionRequest(server).connect().exceptionally(err -> {
          err.printStackTrace();

          if(velocityPlayer.isActive()) {
            velocityPlayer.sendMessage(TextComponent.builder()
              .content("Something went wrong while connecting you to the match.")
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

            matchmakingController.enterQueue(player, gameType);
          }

          return null;
        });
      }
    });
  }

  public void removePlayer(final UUID uuid) {
    if(started) return;

    this.players.remove(uuid);

    Jedis jedis = RedisConnection.getInstance().getRedisConnection();

    this.players.forEach(player -> {
      jedis.publish("highway", "2|" + player.toString() 
        + "|" + this.getPlayersLeft() + "|" + this.getEstimatedWaitTime());
    });

    jedis.close();
  }

  public int getEstimatedWaitTime() {
    if(this.getPlayersLeftToStart() <= 0) {
      return 15;
    } else {
      return -1;
    }
  }
}