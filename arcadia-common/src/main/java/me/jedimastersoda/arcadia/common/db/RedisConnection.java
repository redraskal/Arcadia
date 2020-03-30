package me.jedimastersoda.arcadia.common.db;

import redis.clients.jedis.Jedis;

public class RedisConnection {
  
  private static RedisConnection instance;

  public static RedisConnection getInstance() {
    if(instance == null) {
      instance = new RedisConnection();
    }

    return instance;
  }

  private Jedis jedis;

  private RedisConnection() {}

  public boolean openConnection() {
    if(this.jedis == null || !this.jedis.isConnected()) {
      this.jedis = new Jedis(Credentials.redis_host);

      jedis.connect();

      return true;
    } else {
      return false;
    }
  }

  public Jedis getRedisConnection() {
    if(this.jedis == null || !this.jedis.isConnected()) {
      this.openConnection();
    }

    return this.jedis;
  }

  public boolean closeConnection() {
    if(this.jedis != null && this.jedis.isConnected()) {
      this.jedis.close();

      return true;
    } else {
      return false;
    }
  }
}