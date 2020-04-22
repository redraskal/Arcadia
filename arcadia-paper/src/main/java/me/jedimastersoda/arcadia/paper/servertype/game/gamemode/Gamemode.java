package me.jedimastersoda.arcadia.paper.servertype.game.gamemode;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import me.jedimastersoda.arcadia.paper.servertype.game.GameServerType;
import me.jedimastersoda.arcadia.paper.servertype.game.GameState;
import me.jedimastersoda.arcadia.paper.servertype.game.Map;

public abstract class Gamemode {

  public abstract String id();

  public abstract String name();

  public abstract List<String> description();

  public abstract List<Map> availableMaps();

  @Getter private final GameServerType gameServer;
  @Getter private final Map map;
  @Getter @Setter private GameState gameState = GameState.PRE_GAME;

  public Gamemode(GameServerType gameServer) {
    this.gameServer = gameServer;

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

  public void onPreGame(Player player) {}

  public void onGameStart() {
    this.setGameState(GameState.INGAME);
  }

  public void endGame() {
    this.setGameState(GameState.ENDING);
    this.onGameEnd();
    
    // TODO
  }

  public void onGameEnd() {}
}