package me.jedimastersoda.arcadia.paper.servertype.hub.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.jedimastersoda.arcadia.paper.utils.VelocityUtils;

public class DoubleJumpListener implements Listener {

  private final JavaPlugin javaPlugin;

  public DoubleJumpListener(JavaPlugin javaPlugin) {
    this.javaPlugin = javaPlugin;

    Bukkit.getOnlinePlayers().forEach(player -> {
      player.setFlying(false);
      player.setAllowFlight(true);
    });
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    event.getPlayer().setFlying(false);
    event.getPlayer().setAllowFlight(true);
  }

  @EventHandler
  public void onToggleFlight(PlayerToggleFlightEvent event) {
    event.setCancelled(true);
    event.getPlayer().setAllowFlight(false);

    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_IRON_GOLEM_ATTACK, 1f, 1f);
    VelocityUtils.thrustEntity(event.getPlayer(), 1D);

    new BukkitRunnable() {
      int ticks = 0;

      @Override
      public void run() {
        if(ticks >= 10) {
          if(!event.getPlayer().isOnline() || event.getPlayer().isOnGround()) {
            this.cancel();

            if(event.getPlayer().isOnline()) {
              event.getPlayer().setAllowFlight(true);
            }
          }
        }

        ticks++;
      }
    }.runTaskTimer(this.javaPlugin, 0, 3L);
  }
}