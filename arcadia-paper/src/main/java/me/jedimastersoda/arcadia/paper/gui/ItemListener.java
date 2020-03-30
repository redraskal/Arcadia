package me.jedimastersoda.arcadia.paper.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemListener implements Listener {

  private JavaPlugin javaPlugin;
  private String displayName;
  private ItemCallback itemCallback;

  private boolean allowDrop;
  private boolean allowUse;

  public ItemListener(JavaPlugin javaPlugin, String displayName, boolean allowDrop, boolean allowUse, ItemCallback itemCallback) {
    this.javaPlugin = javaPlugin;
    this.displayName = displayName;
    this.itemCallback = itemCallback;

    this.allowDrop = allowDrop;
    this.allowUse = allowUse;

    this.javaPlugin.getServer().getPluginManager().registerEvents(this, this.javaPlugin);
  }

  @EventHandler
  public void onItemInteract(PlayerInteractEvent playerInteractEvent) {
    try {
      if(playerInteractEvent.getAction() != Action.PHYSICAL) {
        String displayName = ChatColor.stripColor(playerInteractEvent.getItem().getItemMeta().getDisplayName());

        if(displayName.equalsIgnoreCase(this.displayName)) {
          this.itemCallback.callback(playerInteractEvent.getPlayer(), playerInteractEvent.getAction());

          if(!this.allowUse) {
            playerInteractEvent.setCancelled(true);
          }
        }
      }
    } catch (Exception e) {}
  }

  @EventHandler
  public void onItemClick(InventoryClickEvent inventoryClickEvent) {
    try {
      if(inventoryClickEvent.getCurrentItem() != null && inventoryClickEvent.getCurrentItem().getType() != Material.AIR) {
        String displayName = ChatColor.stripColor(inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName());

        if(displayName.equalsIgnoreCase(this.displayName)) {
          inventoryClickEvent.setCancelled(true);
        }
      }
    } catch (Exception e) {}
  }

  @EventHandler
  public void onItemDrop(PlayerDropItemEvent playerDropItemEvent) {
    try {
      if(playerDropItemEvent.getItemDrop() != null) {
        String displayName = ChatColor.stripColor(playerDropItemEvent.getItemDrop().getItemStack().getItemMeta().getDisplayName());

        if(displayName.equalsIgnoreCase(this.displayName)) {
          if(!this.allowDrop) {
            playerDropItemEvent.setCancelled(true);
          }
        }
      }
    } catch (Exception e) {}
  }

  public void unregister() {
    HandlerList.unregisterAll(this);
  }
}