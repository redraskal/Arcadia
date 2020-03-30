package me.jedimastersoda.arcadia.paper.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class JoinItem implements Listener {

  private ItemStack itemStack;
  private int slot;
  private boolean onReload;

  public JoinItem(JavaPlugin javaPlugin, ItemStack itemStack, int slot, boolean onReload) {
    this.itemStack = itemStack;
    this.slot = slot;
    this.onReload = onReload;

    if(this.onReload) {
      for(Player player : Bukkit.getOnlinePlayers()) {
        ItemStack customItem = itemStack.clone();

        if(customItem.getType() == Material.PLAYER_HEAD && customItem.getData().getData() == 3) {
          SkullMeta customMeta = (SkullMeta) customItem.getItemMeta();
          customMeta.setOwner(player.getName());
          customItem.setItemMeta(customMeta);
        }

        player.getInventory().setItem(this.slot, customItem);
      }
    }

    javaPlugin.getServer().getPluginManager().registerEvents(this, javaPlugin);
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    ItemStack customItem = itemStack.clone();

    if(customItem.getType() == Material.PLAYER_HEAD && customItem.getData().getData() == 3) {
      SkullMeta customMeta = (SkullMeta) customItem.getItemMeta();
      customMeta.setOwner(event.getPlayer().getName());
      customItem.setItemMeta(customMeta);
    }

    event.getPlayer().getInventory().setItem(this.slot, customItem);
  }

  public void unregister() {
    HandlerList.unregisterAll(this);
  }
}