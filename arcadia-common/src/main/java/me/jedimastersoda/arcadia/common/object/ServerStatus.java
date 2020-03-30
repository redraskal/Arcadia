package me.jedimastersoda.arcadia.common.object;

import lombok.Getter;

public class ServerStatus {

  @Getter private final String serverName;
  @Getter private final int playerCount;
  @Getter private final int maxPlayerCount;
  @Getter private final String serverType;
  @Getter private final String serverStatusMessage;

  public ServerStatus(String serverName, int playerCount, int maxPlayerCount, String serverType, String serverStatusMessage) {
    this.serverName = serverName;
    this.playerCount = playerCount;
    this.maxPlayerCount = maxPlayerCount;
    this.serverType = serverType;
    this.serverStatusMessage = serverStatusMessage;
  }
}