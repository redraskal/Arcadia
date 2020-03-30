package me.jedimastersoda.arcadia.paper.servertype.hub.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.jedimastersoda.arcadia.paper.servertype.hub.HubServerType;

public class JoinListener implements Listener {

  private final HubServerType hubServerType;

  public JoinListener(HubServerType hubServerType) {
    this.hubServerType = hubServerType;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    event.getPlayer().teleport(this.hubServerType.getSpawnLocation());

    event.setJoinMessage(null);
  }
}