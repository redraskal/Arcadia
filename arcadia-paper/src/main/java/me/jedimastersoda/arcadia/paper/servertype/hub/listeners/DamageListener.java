package me.jedimastersoda.arcadia.paper.servertype.hub.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

  @EventHandler
  public void onPlayerDamage(EntityDamageEvent event) {
    event.setCancelled(true);
  }
}