package org.example.learn.redis.jedis.hello.api;

import org.example.learn.redis.jedis.hello.constant.RedisConstants;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.List;

public class StringApiTest {

    private static final Logger LOG = LoggerFactory.getLogger(StringApiTest.class);

    private static final String KEY = "test:string";
    private static final String VALUE = "changjiang";
    private Jedis jedis;

    @Before
    public void setupJedis() {
        jedis = new Jedis(RedisConstants.HOST, RedisConstants.PORT);
    }


    // ********************************增删改查操作************************************************
    @Test
    public void testSetAndDel() {
        jedis.set(KEY, VALUE);
        jedis.del(KEY);

        jedis.mset(KEY + "001", VALUE + "001", KEY + "002", VALUE + "002");
        List<String> values = jedis.mget(KEY + "001", KEY + "002");
        LOG.info("mget的结果是: {}", values);

        long deletedCoount = jedis.del(KEY + "001", KEY + "002", KEY + "002");
        LOG.info("删除key的个数是: {}", deletedCoount);
    }

    /**
     * setnx是string版本的
     */
    @Test
    public void testSetNX() {
        jedis.set(KEY, VALUE);
        long flag  = jedis.setnx(KEY, VALUE + "001");

        LOG.info("操作是否成功: {}", flag > 0);
    }



    @Test
    public void testIncr() {
        long expected = 10;
        jedis.set("lock:stock", expected + "");
        long newValue = jedis.incr("lock:stock");
        LOG.info("是否CAS操作成功: {}", (expected + 1) == newValue);
    }


}
