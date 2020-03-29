package me.jedimastersoda.arcadia.paper.servertype.hub.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.jedimastersoda.arcadia.paper.servertype.hub.HubServerType;

public class SpawnBoundariesListener implements Listener {

  private final HubServerType hubServerType;

  public SpawnBoundariesListener(HubServerType hubServerType) {
    this.hubServerType = hubServerType;
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerMove(PlayerMoveEvent event) {
    Location to = event.getTo();

    if(to.getY() <= 16) { // player is falling into the void
      event.setTo(this.hubServerType.getSpawnLocation());
    } else {
      if(!this.hubServerType.getSpawnBoundaries().isInBounds(to)) { // player is going outside of the spawn boundaries
        event.setTo(event.getFrom());
      }
    }
  }
}