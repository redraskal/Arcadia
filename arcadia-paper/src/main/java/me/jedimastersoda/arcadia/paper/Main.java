package me.jedimastersoda.arcadia.paper;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.jedimastersoda.arcadia.paper.utils.NMSUtils;

public class Main extends JavaPlugin {

  @Override
  public void onEnable() {
    unregisterCommands();

    // TODO
  }

  private void unregisterCommands() {
    try {
      NMSUtils.unregisterBukkitCommands(new String[] {
        "bukkit", "ver", "restart", "bukkit:version", "plugins",
        "about", "bukkit:plugins", "spigot:restart", "version", "bukkit:ver",
        "bukkit:about", "bukkit:pl", "paper", "spigot:tps", "spigot",
        "bukkit:reload", "rl", "bukkit:timings", "bukkit:rl",
        "pl", "spigot:spigot", "paper:paper"
      });

      NMSUtils.unregisterMinecraftCommands(new String[] {
        "msg", "tell", "w", "me", "help",
        "trigger", "tm", "teammsg", "list"
      });

      new BukkitRunnable() {
        @Override
        public void run() {
          try {
            NMSUtils.unregisterBukkitCommands(new String[] {
              "bukkit:help",
              "help",
              "list",
              "?",
              "bukkit:?"
            });
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }.runTaskLater(this, 1L);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}