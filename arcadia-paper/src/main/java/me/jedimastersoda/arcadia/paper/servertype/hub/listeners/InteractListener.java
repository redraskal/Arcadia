package me.jedimastersoda.arcadia.paper.servertype.hub.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener {

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    // Cancel crop trample
    if(event.getAction() == Action.PHYSICAL) {
      Block soilBlock = event.getClickedBlock();

      if(soilBlock.getType() == Material.FARMLAND) {
        event.setCancelled(true);
      }
    }

    // Cancel door modifications
    if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
      String blockType = event.getClickedBlock().getType().toString();

      if(blockType.contains("DOOR") || blockType.contains("GATE")) {
        event.setCancelled(true);
      }
    }
  }
}