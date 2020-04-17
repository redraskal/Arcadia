package me.jedimastersoda.arcadia.paper.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import lombok.Getter;

public class Point {

  @Getter private final double x;
  @Getter private final double y;
  @Getter private final double z;

  public Point(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Location toBukkit(String world) {
    return new Location(Bukkit.getWorld(world), x, y, z);
  }
}