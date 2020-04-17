package me.jedimastersoda.arcadia.paper.utils;

import lombok.Getter;
import lombok.Setter;

public class AbstractLocationBounds {

  @Getter @Setter Point pointA;
  @Getter @Setter Point pointB;

  public AbstractLocationBounds(Point pointA, Point pointB) {
    this.pointA = pointA;
    this.pointB = pointB;
  }

  public LocationBounds toBukkit(String worldName) {
    return new LocationBounds(pointA.toBukkit(worldName), pointB.toBukkit(worldName));
  }
}