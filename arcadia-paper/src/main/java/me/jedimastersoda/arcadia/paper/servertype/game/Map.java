package me.jedimastersoda.arcadia.paper.servertype.game;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import me.jedimastersoda.arcadia.paper.utils.LocationBounds;
import me.jedimastersoda.arcadia.paper.utils.NMSUtils;
import me.jedimastersoda.arcadia.paper.utils.Point;

public class Map {

  @Getter @Setter public static File mapsLocation;

  @Getter private final String mapName;
  @Getter private final String fileName;

  @Getter @Setter LocationBounds mapBounds;
  @Getter final ArrayList<Point> spawnpoints = new ArrayList<>();
  @Getter private World world;
  private int currentSpawnpoint = 0;

  public Map(String mapName, String fileName) {
    this.mapName = mapName;
    this.fileName = fileName;
  }

  public Map(String mapName, String fileName, LocationBounds mapBounds) {
    this(mapName, fileName);

    setMapBounds(mapBounds);
  }

  public Map(String mapName, String fileName, List<Point> spawnpoints) {
    this(mapName, fileName);

    getSpawnpoints().addAll(spawnpoints);
  }

  public Map(String mapName, String fileName, LocationBounds mapBounds, List<Point> spawnpoints) {
    this(mapName, fileName, mapBounds);

    getSpawnpoints().addAll(spawnpoints);
  }

  public File getFile() {
    return new File(mapsLocation, fileName);
  }

  public void importWorld() throws Exception {
    SlimePlugin slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
    SlimeLoader slimeLoader = slimePlugin.getLoader("file");

    SlimePropertyMap slimePropertyMap = new SlimePropertyMap();
    slimePropertyMap.setString(SlimeProperties.DIFFICULTY, "normal");
    slimePropertyMap.setBoolean(SlimeProperties.PVP, true);

    SlimeWorld slimeWorld = slimePlugin.loadWorld(slimeLoader, getFile().getPath(), true, slimePropertyMap);

    NMSUtils.modifySlimeWorldName(slimeWorld, "game");

    slimePlugin.generateWorld(slimeWorld);

    this.world = Bukkit.getWorld(slimeWorld.getName());

    world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
    world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
    world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
  }

  public boolean hasMapBounds() {
    return mapBounds != null;
  }

  public boolean hasSpawnpoints() {
    return !spawnpoints.isEmpty();
  }

  public void shuffleSpawnpoints() {
    Collections.shuffle(this.spawnpoints);
  }

  public void executeSpawnpoint(Player player) {
    if(currentSpawnpoint >= spawnpoints.size()) {
      currentSpawnpoint = 0;
    }

    player.teleport(spawnpoints.get(currentSpawnpoint).toBukkit("game"));

    currentSpawnpoint++;
  }
}