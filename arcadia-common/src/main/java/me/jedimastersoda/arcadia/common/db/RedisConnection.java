package me.jedimastersoda.arcadia.common.db;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection {
  
  private static RedisConnection instance;

  public static RedisConnection getInstance() {
    if(instance == null) {
      instance = new RedisConnection();
    }

    return instance;
  }

  private JedisPool jedis;

  private RedisConnection() {
    JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setMaxTotal(128);

    this.jedis = new JedisPool(poolConfig, Credentials.redis_host);
  }

  public Jedis getRedisConnection() {
    return this.jedis.getResource();
  }
}