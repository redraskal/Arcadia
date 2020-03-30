package me.jedimastersoda.arcadia.paper.servertype.game;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.jedimastersoda.arcadia.common.highway.RedisServerHighway;
import me.jedimastersoda.arcadia.common.object.ServerStatus;
import me.jedimastersoda.arcadia.paper.servertype.ServerType;

public class GameServerType extends ServerType {

  @Override
  public void onEnable(JavaPlugin javaPlugin, String serverName, String serverType) {
    // TODO

    RedisServerHighway.getInstance().sendServerStatus(
      new ServerStatus(serverName, 0, Bukkit.getMaxPlayers(), serverType, "match_announce")
    );
  }

  @Override
  public void onDisable(JavaPlugin javaPlugin) {}
}