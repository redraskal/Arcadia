package me.jedimastersoda.arcadia.paper.servertype.hub;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.jedimastersoda.arcadia.paper.gui.ItemCallback;
import me.jedimastersoda.arcadia.paper.gui.ItemCreator;
import me.jedimastersoda.arcadia.paper.gui.ItemListener;
import me.jedimastersoda.arcadia.paper.gui.JoinItem;
import me.jedimastersoda.arcadia.paper.matchmaking.MatchmakingClient;
import me.jedimastersoda.arcadia.paper.servertype.ServerType;
import me.jedimastersoda.arcadia.paper.servertype.hub.commands.CancelCommand;
import me.jedimastersoda.arcadia.paper.servertype.hub.commands.PlayCommand;
import me.jedimastersoda.arcadia.paper.servertype.hub.listeners.DamageListener;
import me.jedimastersoda.arcadia.paper.servertype.hub.listeners.DoubleJumpListener;
import me.jedimastersoda.arcadia.paper.servertype.hub.listeners.HungerListener;
import me.jedimastersoda.arcadia.paper.servertype.hub.listeners.InteractListener;
import me.jedimastersoda.arcadia.paper.servertype.hub.listeners.JoinListener;
import me.jedimastersoda.arcadia.paper.servertype.hub.listeners.LeaveListener;
import me.jedimastersoda.arcadia.paper.servertype.hub.listeners.SpawnBoundariesListener;
import me.jedimastersoda.arcadia.paper.servertype.hub.manager.ScoreboardManager;
import me.jedimastersoda.arcadia.paper.utils.LocationBounds;

public class HubServerType extends ServerType {

  @Getter private JavaPlugin javaPlugin;
  @Getter private Location spawnLocation;
  @Getter private LocationBounds spawnBoundaries;

  @Override
  public void onEnable(JavaPlugin javaPlugin, String serverName, String serverType) {
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
    javaPlugin.getServer().getPluginManager().registerEvents(new DoubleJumpListener(this.javaPlugin), this.javaPlugin);
    javaPlugin.getServer().getPluginManager().registerEvents(new LeaveListener(), this.javaPlugin);

    Bukkit.getWorld("world").setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
    Bukkit.getWorld("world").setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
    Bukkit.getWorld("world").setGameRule(GameRule.DO_WEATHER_CYCLE, false);

    ScoreboardManager.initialize(this.javaPlugin, serverName);

    MatchmakingClient.init(this.javaPlugin);
    this.javaPlugin.getCommand("play").setExecutor(new PlayCommand());
    this.javaPlugin.getCommand("cancel").setExecutor(new CancelCommand());
    
    new JoinItem(this.javaPlugin, ItemCreator.createItem(Material.COMPASS, 1, 0, "&3&lGame Selector"), 0, true);

    new ItemListener(this.javaPlugin, "Game Selector", false, false, new ItemCallback() {
      @Override
      public void callback(Player player, Action action) {
        if(action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
          // TODO
        }
      }
    });

    new JoinItem(this.javaPlugin, ItemCreator.createItem(Material.TORCH, 1, 0, "&a&lToggle Players"), 1, true);

    new ItemListener(this.javaPlugin, "Toggle Players", false, false, new ItemCallback() {
      @Override
      public void callback(Player player, Action action) {
        if(action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
          // TODO
        }
      }
    });

    new JoinItem(this.javaPlugin, ItemCreator.createItem(Material.CLOCK, 1, 0, "&2&lLobby Selector"), 8, true);

    new ItemListener(this.javaPlugin, "Lobby Selector", false, false, new ItemCallback() {
      @Override
      public void callback(Player player, Action action) {
        if(action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
          // TODO
        }
      }
    });
  }

  @Override
  public void onDisable(JavaPlugin javaPlugin) {}
}