package me.jedimastersoda.arcadia.paper.servertype.hub.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerListener implements Listener {

  @EventHandler
  public void onHungerChange(FoodLevelChangeEvent event) {
    event.setFoodLevel(20);
  }
}