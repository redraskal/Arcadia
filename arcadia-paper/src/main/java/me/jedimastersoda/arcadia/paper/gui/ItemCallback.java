package me.jedimastersoda.arcadia.paper.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;

public abstract class ItemCallback implements Listener {

  public abstract void callback(Player player, Action action);
}