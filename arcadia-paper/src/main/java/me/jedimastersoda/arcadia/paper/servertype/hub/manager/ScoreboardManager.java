package me.jedimastersoda.arcadia.paper.servertype.hub.manager;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import me.jedimastersoda.arcadia.common.manager.AccountManager;
import me.jedimastersoda.arcadia.common.object.Account;
import me.jedimastersoda.arcadia.paper.utils.BlinkEffect;
import me.jedimastersoda.arcadia.paper.utils.SimpleScoreboard;

public class ScoreboardManager implements Listener {

  private static ScoreboardManager instance = null;

  public static ScoreboardManager getInstance() {
    return instance;
  }

  public static boolean initialize(JavaPlugin javaPlugin, String serverName) {
    if(instance != null) {
      return false;
    }

    instance = new ScoreboardManager(javaPlugin, serverName);

    return true;
  }

  private final JavaPlugin javaPlugin;
  private final String serverName;
  private final Map<Player, SimpleScoreboard> playerScoreboardMap = new HashMap<>();

  private ScoreboardManager(JavaPlugin javaPlugin, String serverName) {
    this.javaPlugin = javaPlugin;
    this.serverName = serverName;

    javaPlugin.getServer().getPluginManager().registerEvents(this, javaPlugin);

    Bukkit.getOnlinePlayers().forEach(player -> createScoreboard(player));
  }

  private void createScoreboard(Player player) {
    player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);

    BlinkEffect effect = new BlinkEffect("ARCADIA", ChatColor.WHITE, ChatColor.AQUA, ChatColor.GRAY);
    effect.next();

    SimpleScoreboard simpleScoreboard = new SimpleScoreboard(effect.next());
    
    try {
      this.setDefaultLines(simpleScoreboard, AccountManager.getInstance().getAccount(player.getUniqueId()));
      simpleScoreboard.update();
      simpleScoreboard.send(player);

      this.playerScoreboardMap.put(player, simpleScoreboard);

      new BukkitRunnable() {
        @Override
        public void run() {
          if(player.isOnline() && player.getScoreboard() != null) {
            player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(effect.next());
          } else {
            this.cancel();
          }
        }
      }.runTaskTimer(this.javaPlugin, 0, 3L);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setDefaultLines(SimpleScoreboard board, Account account) {
    board.reset();

    board.add("&1", 7);
    board.add("&bRank » " + account.getRank().getTag(), 6);
    board.add("&3Coins » &70", 5);
    board.add("&2", 4);
    board.add("&eOnline » &7" + Bukkit.getOnlinePlayers().size(), 3);
    board.add("&6Server » &7" + this.serverName, 2);
    board.add("&3", 1);
    board.add("&bwww.arcadia.com", 0);
  }

  private void updatePlayerCount() {
    this.playerScoreboardMap.values().forEach(simpleScoreboard -> {
      simpleScoreboard.add("&eOnline » &7" + Bukkit.getOnlinePlayers().size(), 3);

      simpleScoreboard.update();
    });
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    updatePlayerCount();

    this.createScoreboard(event.getPlayer());
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    this.playerScoreboardMap.remove(event.getPlayer());

    updatePlayerCount();
  }
}