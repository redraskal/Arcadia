package me.jedimastersoda.arcadia.paper.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.jedimastersoda.arcadia.common.manager.AccountManager;
import me.jedimastersoda.arcadia.common.object.Account;

public class ChatListener implements Listener {

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    event.setCancelled(true);

    try {
      Account account = AccountManager.getInstance().getAccount(event.getPlayer().getUniqueId());
      String formattedMessage = account.getRank().formatChatMessage(event.getPlayer().getName(), 
        ChatColor.stripColor(event.getMessage()));

      Bukkit.broadcastMessage(formattedMessage);
    } catch (Exception e) {
      e.printStackTrace();

      event.getPlayer().sendMessage(ChatColor.RED + "Your message could not be sent due to a database error.");
    }
  }
}