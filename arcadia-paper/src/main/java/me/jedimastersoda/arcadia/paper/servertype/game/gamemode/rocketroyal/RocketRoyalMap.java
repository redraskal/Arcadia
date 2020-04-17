package me.jedimastersoda.arcadia.paper.servertype.game.gamemode.rocketroyal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.Getter;
import me.jedimastersoda.arcadia.paper.servertype.game.Map;
import me.jedimastersoda.arcadia.paper.utils.Point;

public class RocketRoyalMap extends Map {

  @Getter private final List<Point> endCrystalSpawnpoints;
  @Getter private final List<RocketRoyalEndCrystal> rocketRoyalEndCrystals = new ArrayList<>();

  public RocketRoyalMap(String mapName, String fileName, List<Point> spawnpoints, List<Point> endCrystalSpawnpoints) {
    super(mapName, fileName, spawnpoints);

    this.endCrystalSpawnpoints = endCrystalSpawnpoints;
  }

  public void spawnEndCrystals() {
    endCrystalSpawnpoints.forEach(endCrystalSpawnpoint -> 
      rocketRoyalEndCrystals.add(new RocketRoyalEndCrystal(endCrystalSpawnpoint.toBukkit("game")))
    );
  }

  public void spawnPowerup() {
    rocketRoyalEndCrystals.get(new Random().nextInt(rocketRoyalEndCrystals.size())).spawnPowerup();
  }
}