package me.jedimastersoda.arcadia.common.highway;

import java.util.ArrayList;
import java.util.List;

import me.jedimastersoda.arcadia.common.db.RedisConnection;
import me.jedimastersoda.arcadia.common.object.ServerStatus;
import redis.clients.jedis.JedisPubSub;

public class RedisServerHighway {

  private static RedisServerHighway instance;

  public static RedisServerHighway getInstance() {
    if(instance == null) {
      instance = new RedisServerHighway();
    }

    return instance;
  }

  private final JedisPubSub jedisPubSub;
  private List<HighwayListener> highwayListeners = new ArrayList<>();

  private RedisServerHighway() {
    this.jedisPubSub = new JedisPubSub() {
      @Override
      public void onMessage(String channel, String message) {
        String[] messageFragments = message.split("|");
        ServerStatus serverStatus = new ServerStatus(messageFragments[0], 
          Integer.parseInt(messageFragments[1]), Integer.parseInt(messageFragments[2]), 
          messageFragments[3], messageFragments[4]);
        
        highwayListeners.forEach(listener -> listener.onServerUpdate(serverStatus));
      }
    };

    new Thread(new Runnable() {
      @Override
      public void run() {
        RedisConnection.getInstance().getRedisConnection().subscribe(jedisPubSub, "highway");
      }
    }, "highwaySubcribeThread").start();
  }

  public void registerListener(HighwayListener highwayListener) {
    this.highwayListeners.add(highwayListener);
  }

  public void unregisterListener(HighwayListener highwayListener) {
    this.highwayListeners.remove(highwayListener);
  }

  public void sendServerStatus(ServerStatus serverStatus) {
    String message = serverStatus.getServerName() + "|" + serverStatus.getPlayerCount() 
      + "|" + serverStatus.getMaxPlayerCount() + "|" + serverStatus.getServerType() 
      + "|" + serverStatus.getServerStatusMessage();
    
    RedisConnection.getInstance().getRedisConnection().publish("highway", message);
  }
}