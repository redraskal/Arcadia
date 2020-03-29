package me.jedimastersoda.arcadia.paper.utils;

import com.google.common.base.Preconditions;

import org.bukkit.Location;

import lombok.Getter;

public class LocationBounds {

  @Getter final Location pointA;
  @Getter final Location pointB;

  private final int xMax;
  private final int xMin;
  private final int yMax;
  private final int yMin;
  private final int zMax;
  private final int zMin;

  public LocationBounds(Location pointA, Location pointB) {
    Preconditions.checkArgument(pointA.getWorld().getName() == pointB.getWorld().getName(), "Both points must be located in the same dimension.");

    this.pointA = pointA;
    this.pointB = pointB;

    this.xMax = Math.max(pointA.getBlockX(), pointB.getBlockX());
    this.xMin = Math.min(pointA.getBlockX(), pointB.getBlockX());
    this.yMax = Math.max(pointA.getBlockY(), pointB.getBlockY());
    this.yMin = Math.min(pointA.getBlockY(), pointB.getBlockY());
    this.zMax = Math.max(pointA.getBlockZ(), pointB.getBlockZ());
    this.zMin = Math.min(pointA.getBlockZ(), pointB.getBlockZ());
  }

  public boolean isInBounds(Location location) {
    if(location.getWorld().getName() == pointA.getWorld().getName()) {
      return this.xMin <= location.getX() && this.xMax >= location.getX() 
        && this.yMin <= location.getY() && this.yMax >= location.getY() 
        && this.zMin <= location.getZ() && this.zMax >= location.getZ();
    } else {
      return false;
    }
  }
}