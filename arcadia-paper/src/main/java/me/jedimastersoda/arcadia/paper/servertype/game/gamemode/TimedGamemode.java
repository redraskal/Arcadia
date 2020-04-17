package me.jedimastersoda.arcadia.paper.servertype.game.gamemode;

import lombok.Getter;
import me.jedimastersoda.arcadia.common.object.Time;

public abstract class TimedGamemode extends Gamemode {

  @Getter private final Time gameMaxTime;

  public TimedGamemode(Time gameMaxTime) {
    this.gameMaxTime = gameMaxTime;
  }

  public abstract void onTimeUpdate(Time newTime);

  @Override
  public void onGameStart() {
    // TODO
  }
}