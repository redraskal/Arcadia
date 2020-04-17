package me.jedimastersoda.arcadia.paper.servertype.game.runnable;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class GameStartRunnable extends BukkitRunnable {

  private int seconds = 0;

  @Override
  public void run() {
    switch(seconds) {
      case 0: {
        

        // Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(title));
      }
    }
  }
}