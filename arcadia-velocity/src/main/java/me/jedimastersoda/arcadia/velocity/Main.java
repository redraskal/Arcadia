package me.jedimastersoda.arcadia.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

import lombok.Getter;
import me.jedimastersoda.arcadia.velocity.listener.PlayerJoinListener;
import me.jedimastersoda.arcadia.velocity.listener.PlayerPreConnectListener;
import me.jedimastersoda.arcadia.velocity.matchmaking.MatchmakingController;

public class Main {

  @Getter private final ProxyServer proxyServer;
  @Getter private final Logger logger;

  @Inject
  public Main(ProxyServer proxyServer, Logger logger) {
    this.proxyServer = proxyServer;
    this.logger = logger;
  }

  @Subscribe
  public void onProxyInitialization(ProxyInitializeEvent event) {
    this.getLogger().info("Starting Arcadia plugin.");

    this.getProxyServer().getCommandManager().unregister("velocity");

    this.getProxyServer().getEventManager().register(this, new PlayerPreConnectListener());
    this.getProxyServer().getEventManager().register(this, new PlayerJoinListener());

    MatchmakingController.init(this);
  }
}