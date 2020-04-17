package me.jedimastersoda.arcadia.paper.servertype.game.gamemode.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.jedimastersoda.arcadia.paper.servertype.game.GameServerType;

public class JoinListener implements Listener {

  private final GameServerType gameServerType;

  public JoinListener(GameServerType gameServerType) {
    this.gameServerType = gameServerType;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    gameServerType.getGamemode().onPreGame(event.getPlayer());
  }
}