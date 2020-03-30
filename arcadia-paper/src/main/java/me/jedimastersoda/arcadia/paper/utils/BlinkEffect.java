package me.jedimastersoda.arcadia.paper.utils;

import org.bukkit.ChatColor;

public class BlinkEffect {

  private String objective;

  private String name;
  private ChatColor currentColor;
  private ChatColor otherColor;
  private ChatColor c33;
  private int index = 0;

  private boolean r = false;

  public BlinkEffect(String title, ChatColor bgColor, ChatColor mainColor, ChatColor waveColor) {
    this.currentColor = bgColor;
    this.otherColor = mainColor;
    this.c33 = waveColor;
    this.name = title;
  }

  private void nextP() {
    if(r && index == name.length()) {
      //Nope
    } else {
      if(index < name.length() && index > 0) {
        objective = otherColor.toString() + ChatColor.BOLD + name.substring(0, index)
          + currentColor.toString() + ChatColor.BOLD + name.substring(index, index)
          + c33.toString() + ChatColor.BOLD + name.substring(index, index+1)
          + currentColor.toString() + ChatColor.BOLD + name.substring(index+1, name.length());
      }

      if(index == name.length() && !r) {
        objective = otherColor.toString() + ChatColor.BOLD + name.substring(0, name.length());
        ChatColor tempColor = currentColor;
        currentColor = otherColor;
        otherColor = tempColor;
      }

      if(index == name.length() + 40 && !r) {
        index = 0;
      }

      index++;
    }
  }

  public String getDefault() {
    return this.name;
  }

  public void reset() {
    index = 0;
  }

  public void dontRepeat() {
    r = true;
  }

  public String next() {
    nextP();
    return "" + objective;
  }
}