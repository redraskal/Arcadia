package me.jedimastersoda.arcadia.paper.servertype.hub.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.jedimastersoda.arcadia.paper.matchmaking.MatchmakingClient;

public class CancelCommand implements CommandExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
      @NotNull String[] args) {
    if(sender instanceof Player) {
      Player player = (Player) sender;

      if(MatchmakingClient.getInstance().leaveMatchmaking(player)) {
        player.sendMessage(ChatColor.BLUE + "You have left the matchmaking lobby.");
      } else {
        player.sendMessage(ChatColor.RED + "You are not in a matchmaking lobby.");
      }
    }
    
    return false;
  }
}