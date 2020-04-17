package me.jedimastersoda.arcadia.paper.servertype.game.gamemode;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.Getter;
import me.jedimastersoda.arcadia.paper.servertype.game.Map;

public abstract class Gamemode {

  public abstract String id();

  public abstract String name();

  public abstract List<String> description();

  public abstract List<Map> availableMaps();

  public abstract void onPreGame(Player player);

  public abstract void onGameStart();

  public abstract void onGameEnd();

  @Getter private final Map map;

  public Gamemode() {
    if (availableMaps() != null && !availableMaps().isEmpty()) {
      this.map = availableMaps().get(new Random().nextInt(availableMaps().size()));
    } else {
      this.map = null;
    }
  }

  public void onSetup() {
    if (this.map != null) {
      try {
        this.map.importWorld();

        if(this.map.hasSpawnpoints()) {
          this.map.shuffleSpawnpoints();
        }
      } catch (Exception e) {
        e.printStackTrace();

        Bukkit.shutdown();
      }
    }
  }

  public void endGame() {
    // TODO
  }
}