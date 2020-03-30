package me.jedimastersoda.arcadia.paper.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.jedimastersoda.arcadia.common.highway.RedisServerHighway;
import me.jedimastersoda.arcadia.common.object.ServerStatus;
import me.jedimastersoda.arcadia.paper.Main;

public class JoinListener implements Listener {

  private final Main main;

  public JoinListener(Main main) {
    this.main = main;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    RedisServerHighway.getInstance().sendServerStatus(new ServerStatus(
      main.getServerName(), Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers(), 
      main.getServerTypeString(), "join")
    );
  }
}