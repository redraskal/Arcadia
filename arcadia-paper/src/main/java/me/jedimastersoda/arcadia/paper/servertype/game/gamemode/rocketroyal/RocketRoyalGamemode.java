package me.jedimastersoda.arcadia.paper.servertype.game.gamemode.rocketroyal;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

import me.jedimastersoda.arcadia.common.object.Time;
import me.jedimastersoda.arcadia.paper.servertype.game.Map;
import me.jedimastersoda.arcadia.paper.servertype.game.gamemode.TimedGamemode;
import me.jedimastersoda.arcadia.paper.utils.AbstractLocationBounds;
import me.jedimastersoda.arcadia.paper.utils.Point;

public class RocketRoyalGamemode extends TimedGamemode {

  public RocketRoyalGamemode() {
    super(new Time(5, 0));
  }

  @Override
  public String id() {
    return "rr";
  }

  @Override
  public String name() {
    return "Rocket Royal";
  }

  @Override
  public List<String> description() {
    return Arrays.asList(
      "&bShoot players with rockets",
      "&band steal all the power-ups!",
      "",
      "&e&lLeft-Click &7to shoot fireballs",
      "&e&lRight-Click &7to grapple onto blocks"
    );
  }

  @Override
  public List<Map> availableMaps() {
    return Arrays.asList(
      new RocketRoyalMap(
        "Forest Grounds", "rocketroyal", 
        Arrays.asList(
          new Point(-45.5, 69, 28.5),
          new Point(-37.5, 70, -25.5),
          new Point(-9.5, 69, -53.5),
          new Point(45.5, 69, -31.5)
        ),
        Arrays.asList(
          new Point(-37, 70, -13),
          new Point(-6, 90, 16),
          new Point(-25, 89, -32),
          new Point(16, 72, -12),
          new Point(-10, 69, -52)
        )
      )
    );
  }

  @Override
  public void onSetup() {
    super.onSetup();

    ((RocketRoyalMap) this.getMap()).spawnEndCrystals();
  }

  @Override
  public void onPreGame(Player player) {
    // TODO: Scoreboard

    this.getMap().executeSpawnpoint(player);
  }

  @Override
  public void onGameStart() {
    super.onGameStart();

    // TODO
  }

  @Override
  public void onTimeUpdate(Time newTime) {
    // TODO
  }

  @Override
  public void onGameEnd() {
    // TODO
  }
}