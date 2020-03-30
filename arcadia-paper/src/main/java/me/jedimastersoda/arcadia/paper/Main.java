package me.jedimastersoda.arcadia.paper;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import me.jedimastersoda.arcadia.paper.servertype.hub.HubServerType;
import me.jedimastersoda.arcadia.common.highway.RedisServerHighway;
import me.jedimastersoda.arcadia.common.object.ServerStatus;
import me.jedimastersoda.arcadia.paper.listeners.ChatListener;
import me.jedimastersoda.arcadia.paper.listeners.JoinListener;
import me.jedimastersoda.arcadia.paper.listeners.LeaveListener;
import me.jedimastersoda.arcadia.paper.servertype.ServerType;
import me.jedimastersoda.arcadia.paper.utils.NMSUtils;

public class Main extends JavaPlugin {

  @Getter private String serverName;
  @Getter private ServerType serverType;
  @Getter private String serverTypeString;

  @Override
  public void onEnable() {
    unregisterCommands();

    Map<String, Class<? extends ServerType>> serverTypes = new HashMap<>();

    serverTypes.put("hub", HubServerType.class);

    this.serverName = this.getConfig().getString("server-name");

    this.getServer().getPluginManager().registerEvents(new ChatListener(), this);
    this.getServer().getPluginManager().registerEvents(new LeaveListener(this), this);
    this.getServer().getPluginManager().registerEvents(new JoinListener(this), this);

    try {
      this.serverTypeString = this.getConfig().getString("server-type");
      this.serverType = serverTypes.get(serverTypeString).newInstance();

      RedisServerHighway.getInstance().sendServerStatus(new ServerStatus(
        this.serverName, Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers(), 
        this.serverTypeString, "start")
      );

      this.getServerType().onEnable(this, this.serverName);
    } catch (Exception e) {
      e.printStackTrace();

      Bukkit.shutdown();
    }
  }

  public void onDisable() {
    this.getServerType().onDisable(this);

    RedisServerHighway.getInstance().sendServerStatus(new ServerStatus(
      this.serverName, Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers(), 
      this.serverTypeString, "shutdown")
    );
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