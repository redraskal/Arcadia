package me.jedimastersoda.arcadia.common.object;

import lombok.Getter;

public enum Rank {

  DEFAULT(0, "§7", "§7DEFAULT"),
  ADMIN(1, "§c[ADMIN] ", "§c[ADMIN]");

  @Getter private final int ranking;
  private final String chatPrefix;
  @Getter private final String tag;

  private Rank(int ranking, String chatPrefix, String tag) {
    this.ranking = ranking;
    this.chatPrefix = chatPrefix;
    this.tag = tag;
  }

  public boolean hasPermission(Rank compareTo) {
    return this.ranking >= compareTo.getRanking();
  }

  public String formatChatMessage(String username, String message) {
    return this.chatPrefix + username + "§f: " + message;
  }
}