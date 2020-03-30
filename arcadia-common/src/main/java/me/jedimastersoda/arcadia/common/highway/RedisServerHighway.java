package me.jedimastersoda.arcadia.common.highway;

import java.util.ArrayList;
import java.util.List;

import me.jedimastersoda.arcadia.common.db.RedisConnection;
import me.jedimastersoda.arcadia.common.object.ServerStatus;
import redis.clients.jedis.Jedis;
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
        if(!channel.equalsIgnoreCase("highway")) return;

        String[] messageFragments = message.split("\\|"); // kill me, i didn't realize this was a regular expressions thing and used |
        
        if(messageFragments[0].equals("0")) {
          ServerStatus serverStatus = new ServerStatus(messageFragments[1], 
            Integer.parseInt(messageFragments[2]), Integer.parseInt(messageFragments[3]), 
            messageFragments[4], messageFragments[5]);
        
          highwayListeners.forEach(listener -> listener.onServerUpdate(serverStatus));
        } else {
          highwayListeners.forEach(listener -> listener.onMessage(messageFragments));
        }
      }
    };

    new Thread(new Runnable() {
      @Override
      public void run() {
        RedisConnection.getInstance().getRedisConnection().subscribe(jedisPubSub, "highway");
      }
    }, "highwaySubscribeThread").start();
  }

  public void registerListener(HighwayListener highwayListener) {
    this.highwayListeners.add(highwayListener);
  }

  public void unregisterListener(HighwayListener highwayListener) {
    this.highwayListeners.remove(highwayListener);
  }

  public void sendServerStatus(ServerStatus serverStatus) {
    String message = "0|" + serverStatus.getServerName() + "|" + serverStatus.getPlayerCount() 
      + "|" + serverStatus.getMaxPlayerCount() + "|" + serverStatus.getServerType() 
      + "|" + serverStatus.getServerStatusMessage();
    
    Jedis jedis = RedisConnection.getInstance().getRedisConnection();
    jedis.publish("highway", message);
    jedis.close();
  }
}