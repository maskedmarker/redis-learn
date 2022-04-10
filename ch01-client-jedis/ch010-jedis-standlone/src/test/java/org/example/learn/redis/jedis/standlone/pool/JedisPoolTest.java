package org.example.learn.redis.jedis.standlone.pool;

import org.example.learn.redis.jedis.standlone.config.RedisConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolTest {

    public static void main(String[] args) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);
        JedisPool pool = new JedisPool(jedisPoolConfig, RedisConfig.HOST, RedisConfig.PORT);

        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            jedis.set("pooledJedis", "hello jedis pool!");
            System.out.println(jedis.get("pooledJedis"));
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            //还回pool中
            if(jedis != null){
                jedis.close();
            }
        }

        pool.close();
    }
}
