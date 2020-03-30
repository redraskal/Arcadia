package me.jedimastersoda.arcadia.paper;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import me.jedimastersoda.arcadia.paper.servertype.hub.HubServerType;
import me.jedimastersoda.arcadia.paper.listeners.ChatListener;
import me.jedimastersoda.arcadia.paper.servertype.ServerType;
import me.jedimastersoda.arcadia.paper.utils.NMSUtils;

public class Main extends JavaPlugin {

  @Getter private String serverName;
  @Getter private ServerType serverType;

  @Override
  public void onEnable() {
    unregisterCommands();

    Map<String, Class<? extends ServerType>> serverTypes = new HashMap<>();

    serverTypes.put("hub", HubServerType.class);

    this.serverName = this.getConfig().getString("server-name");

    this.getServer().getPluginManager().registerEvents(new ChatListener(), this);

    try {
      this.serverType = serverTypes.get(this.getConfig().getString("server-type")).newInstance();

      this.getServerType().onEnable(this, this.serverName);
    } catch (Exception e) {
      e.printStackTrace();

      Bukkit.shutdown();
    }
  }

  public void onDisable() {
    this.getServerType().onDisable(this);
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