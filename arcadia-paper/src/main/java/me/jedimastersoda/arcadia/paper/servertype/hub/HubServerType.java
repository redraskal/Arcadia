package me.jedimastersoda.arcadia.paper.servertype.hub;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.jedimastersoda.arcadia.paper.servertype.ServerType;
import me.jedimastersoda.arcadia.paper.servertype.hub.listeners.DamageListener;
import me.jedimastersoda.arcadia.paper.servertype.hub.listeners.HungerListener;
import me.jedimastersoda.arcadia.paper.servertype.hub.listeners.InteractListener;
import me.jedimastersoda.arcadia.paper.servertype.hub.listeners.JoinListener;
import me.jedimastersoda.arcadia.paper.servertype.hub.listeners.SpawnBoundariesListener;
import me.jedimastersoda.arcadia.paper.utils.LocationBounds;

public class HubServerType extends ServerType {

  @Getter private JavaPlugin javaPlugin;
  @Getter private Location spawnLocation;
  @Getter private LocationBounds spawnBoundaries;

  @Override
  public void onEnable(JavaPlugin javaPlugin) {
    this.javaPlugin = javaPlugin;
    this.spawnLocation = new Location(Bukkit.getWorld("world"), 41.5, 105, 245.5, -90, 1);
    this.spawnBoundaries = new LocationBounds(
      new Location(Bukkit.getWorld("world"), 6, 152, 164), 
      new Location(Bukkit.getWorld("world"), 156, 16, 313)
    );

    javaPlugin.getServer().getPluginManager().registerEvents(new JoinListener(this), this.javaPlugin);
    javaPlugin.getServer().getPluginManager().registerEvents(new InteractListener(), this.javaPlugin);
    javaPlugin.getServer().getPluginManager().registerEvents(new SpawnBoundariesListener(this), this.javaPlugin);
    javaPlugin.getServer().getPluginManager().registerEvents(new DamageListener(), this.javaPlugin);
    javaPlugin.getServer().getPluginManager().registerEvents(new HungerListener(), this.javaPlugin);

    Bukkit.getWorld("world").setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
    Bukkit.getWorld("world").setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
    Bukkit.getWorld("world").setGameRule(GameRule.DO_WEATHER_CYCLE, false);
  }

  @Override
  public void onDisable(JavaPlugin javaPlugin) {}
}