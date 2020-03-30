package me.jedimastersoda.arcadia.paper.servertype;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class ServerType {

  public abstract void onEnable(JavaPlugin javaPlugin, String serverName);

  public abstract void onDisable(JavaPlugin javaPlugin);

}