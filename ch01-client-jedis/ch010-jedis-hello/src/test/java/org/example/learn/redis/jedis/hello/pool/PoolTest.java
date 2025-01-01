package org.example.learn.redis.jedis.hello.pool;

import org.example.learn.redis.jedis.hello.constant.RedisConstants;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.Instant;

public class PoolTest {

    private static final Logger LOG = LoggerFactory.getLogger(PoolTest.class);

    private static final String KEY = "test:key";
    private static final String VALUE = "changjiang";

    private JedisPool jedisPool;

    @Before
    public void setup() {
        jedisPool = new JedisPool(RedisConstants.HOST, RedisConstants.PORT);
    }

    @Test
    public void testKey() {
        // Use a Jedis instance from the pool
        Jedis jedis = jedisPool.getResource();

        //删除所以的key是否成功
        LOG.info("清空所有key, 执行操作的结果: {}", jedis.flushAll());

        //执行操作的结果返回类型是Boolean
        LOG.info("判断某个key是否存在, 执行操作的结果: {}", jedis.exists(KEY));

        LOG.info("新增键值对<username:0001, zhangsan>, 执行操作的结果: {}", jedis.set("username:0001", "zhangsan"));

        //
        LOG.info("查看键username:0001保存的value所对应的类型, 执行操作的结果: {}", jedis.type("username:0001"));


        //执行操作的结果返回类型是Long,表示有多少个key被删除了
        LOG.info("删除键username:0001, 执行操作的结果: {}", jedis.del("username:0001"));

        //执行操作的结果返回类型是Set
        LOG.info("查看redis中所有的key, 执行操作的结果: {}", jedis.keys("*"));

    }
}
