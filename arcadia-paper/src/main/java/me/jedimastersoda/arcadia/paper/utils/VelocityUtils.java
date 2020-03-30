package me.jedimastersoda.arcadia.paper.utils;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class VelocityUtils {

  public static void thrustEntity(Entity entity, double thrust) {
    entity.setVelocity(entity.getLocation().getDirection().multiply(1.6D * thrust).setY(1.0D * thrust));
  }

  public static void flingEntity(Entity entity, double force) {
    double x = -Math.sin(Math.toRadians(entity.getLocation().getYaw())) *
      Math.cos(Math.toRadians(entity.getLocation().getPitch()));
    double z = Math.cos(Math.toRadians(entity.getLocation().getYaw())) *
      Math.cos(Math.toRadians(entity.getLocation().getPitch()));
    double y = -Math.sin(Math.toRadians(entity.getLocation().getPitch()));

    entity.setVelocity(new Vector(x, y*2, z).multiply(-force));
  }
}