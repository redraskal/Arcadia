package me.jedimastersoda.arcadia.paper.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.jedimastersoda.arcadia.common.highway.RedisServerHighway;
import me.jedimastersoda.arcadia.common.manager.AccountManager;
import me.jedimastersoda.arcadia.common.object.ServerStatus;
import me.jedimastersoda.arcadia.paper.Main;

public class LeaveListener implements Listener {

  private final Main main;

  public LeaveListener(Main main) {
    this.main = main;
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    AccountManager.getInstance().clearAccountFromCache(event.getPlayer().getUniqueId());

    RedisServerHighway.getInstance().sendServerStatus(new ServerStatus(
      main.getServerName(), Bukkit.getOnlinePlayers().size() - 1, Bukkit.getMaxPlayers(), 
      main.getServerTypeString(), "disconnect")
    );
  }
}