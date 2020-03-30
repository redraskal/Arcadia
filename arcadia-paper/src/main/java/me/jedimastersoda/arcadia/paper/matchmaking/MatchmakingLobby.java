package me.jedimastersoda.arcadia.paper.matchmaking;

import org.bukkit.ChatColor;

import lombok.Getter;
import lombok.Setter;
import me.jedimastersoda.arcadia.common.object.Time;

public class MatchmakingLobby {

  @Getter @Setter private int playersLeft;
  @Getter @Setter private int estimatedWaitTime; // est. wait time in seconds, -1 for unknown

  private int animationFrame = -1;
  private boolean animationDirection = true;

  public MatchmakingLobby(int playersLeft, int estimatedWaitTime) {
    this.playersLeft = playersLeft;
    this.estimatedWaitTime = estimatedWaitTime;
  }

  public String getActionBarMessage() {
    if(animationDirection) {
      if(animationFrame >= 2) {
        animationDirection = false;
        animationFrame--;
      } else {
        animationFrame++;
      }
    } else {
      if(animationFrame <= 0) {
        animationDirection = true;
        animationFrame++;
      } else {
        animationFrame--;
      }
    }

    String searchBubble = "";

    switch(animationFrame) {
      case 0:
        searchBubble = "0oo";
        break;
      case 1:
        searchBubble = "o0o";
        break;
      case 2:
        searchBubble = "oo0";
        break;
    }

    if(estimatedWaitTime > -1) {
      return ChatColor.translateAlternateColorCodes('&', "&b&lSearching for players " + searchBubble + " &b| &f" + playersLeft + " left &b| &f<" + new Time(estimatedWaitTime).format());
    } else {
      if(playersLeft != -1) {
        return ChatColor.translateAlternateColorCodes('&', "&b&lSearching for players " + searchBubble + " &b| &f" + playersLeft + " left");
      } else {
        return ChatColor.translateAlternateColorCodes('&', "&b&lSearching for lobby " + searchBubble);
      }
    }
  }
}