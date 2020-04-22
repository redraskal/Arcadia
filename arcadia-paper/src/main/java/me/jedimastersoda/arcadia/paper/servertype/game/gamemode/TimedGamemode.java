package me.jedimastersoda.arcadia.paper.servertype.game.gamemode;

import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import me.jedimastersoda.arcadia.common.object.Time;
import me.jedimastersoda.arcadia.paper.servertype.game.GameServerType;
import me.jedimastersoda.arcadia.paper.servertype.game.GameState;

public abstract class TimedGamemode extends Gamemode {

  @Getter private final Time gameMaxTime;
  @Getter private Time gameTime;

  public TimedGamemode(GameServerType gameServer, Time gameMaxTime) {
    super(gameServer);

    this.gameMaxTime = gameMaxTime;
    this.gameTime = gameMaxTime;
  }

  public abstract void onTimeUpdate(Time newTime);

  @Override
  public void onGameStart() {
    super.onGameStart();

    new BukkitRunnable() {
      @Override
      public void run() {
        if(getGameState() == GameState.INGAME) {
          int newMinutes = gameTime.getMinutes();
          int newSeconds = gameTime.getSeconds() - 1;

          if(newSeconds < 0) {
            newMinutes--;
            newSeconds = 59;
          }

          gameTime = new Time(newMinutes, newSeconds);

          onTimeUpdate(gameTime);

          if(gameTime.getMinutes() == 0 && gameTime.getSeconds() == 0) {
            this.cancel();

            endGame();
          }
        } else {
          this.cancel();
        }
      }
    }.runTaskTimer(this.getGameServer().getJavaPlugin(), 0, 20L);
  }
}