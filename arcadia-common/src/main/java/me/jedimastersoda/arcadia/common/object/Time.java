package me.jedimastersoda.arcadia.common.object;

import lombok.Getter;

public class Time {

  @Getter private final int minutes;
  @Getter private final int seconds;

  public Time(int seconds) {
    this.minutes = (int) Math.floor(Integer.valueOf(seconds).doubleValue() / 60D);
    this.seconds = seconds - (this.minutes * 60);
  }

  public Time(int minutes, int seconds) {
    this.minutes = minutes;
    this.seconds = seconds;
  }

  public String format() {
    String output = "";

    if(minutes == 0 && seconds == 0) {
      output = "0s";
    } else {
      if(minutes > 0) {
        output += minutes + "m";
      }

      if(seconds > 0) {
        output += seconds + "s";
      }
    }

    return output;
  }
}