package org.example.learn.redis.standalone.jedis;

import redis.clients.jedis.Jedis;

/**
 * jedis操作单机redis操作
 */
public class JedisSetupDemo {


    public static void main(String[] args) {

        Jedis jedis = new Jedis("192.168.56.101", 6379);
        jedis.set("foo", "bar");
        String value = jedis.get("foo");

        System.out.println("value = " + value);

    }
}
