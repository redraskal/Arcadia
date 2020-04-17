package me.jedimastersoda.arcadia.paper.servertype.game;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.jedimastersoda.arcadia.common.highway.RedisServerHighway;
import me.jedimastersoda.arcadia.common.object.ServerStatus;
import me.jedimastersoda.arcadia.paper.servertype.ServerType;
import me.jedimastersoda.arcadia.paper.servertype.game.gamemode.Gamemode;
import me.jedimastersoda.arcadia.paper.servertype.game.gamemode.listeners.JoinListener;
import me.jedimastersoda.arcadia.paper.servertype.game.gamemode.rocketroyal.RocketRoyalGamemode;

public class GameServerType extends ServerType {

  @Getter private String serverName;
  @Getter private Gamemode gamemode;
  @Getter private String gamemodeTypeString;

  @Override
  public void onEnable(JavaPlugin javaPlugin, String serverName, String serverType) {
    this.serverName = serverName;
    this.gamemodeTypeString = serverType.split("game_")[1];

    Map.setMapsLocation(new File(javaPlugin.getConfig().getString("maps-location")));

    java.util.Map<String, Class<? extends Gamemode>> gamemodeTypes = new HashMap<>();

    gamemodeTypes.put("rr", RocketRoyalGamemode.class);

    try {
      this.gamemode = gamemodeTypes.get(gamemodeTypeString).newInstance();

      javaPlugin.getServer().getPluginManager().registerEvents(new JoinListener(this), javaPlugin);

      gamemode.onSetup();

      RedisServerHighway.getInstance().sendServerStatus(
        new ServerStatus(serverName, 0, Bukkit.getMaxPlayers(), serverType, "match_announce")
      );
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onDisable(JavaPlugin javaPlugin) {}
}