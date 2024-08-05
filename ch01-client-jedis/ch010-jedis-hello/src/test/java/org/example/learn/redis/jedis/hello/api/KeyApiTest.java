package org.example.learn.redis.jedis.hello.api;

import org.example.learn.redis.jedis.hello.constant.RedisConstants;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.time.Instant;

public class KeyApiTest {

    private static final Logger LOG = LoggerFactory.getLogger(KeyApiTest.class);

    private static final String KEY = "test:key";
    private static final String VALUE = "changjiang";
    private Jedis jedis;

    @Before
    public void setupJedis() {
        jedis = new Jedis(RedisConstants.HOST, RedisConstants.PORT);
    }

    @Test
    public void testKey() {
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

    @Test
    public void testTTL() {
        jedis.del("expireKey");
        jedis.set("expireKey", "0001");

        //执行操作的结果表示 1: the timeout was set. 0: the timeout was not set(key已经过期了)
        LOG.info("设置key的超时时间, 执行操作的结果: {}", jedis.expire("expireKey", 10));
        LOG.info("设置key的超时时间, 执行操作的结果: {}", jedis.expire("expireKey", 10));

        LOG.info("设置key在某个时间点超时, 执行操作的结果: {}", jedis.expireAt("expireKey", Instant.now().getEpochSecond() + 8));

        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //Integer reply, returns the remaining time to live in seconds of a key that has an EXPIRE.
        //等于0表示已经失效了,等于-1/-2表示没有设置失效时间或key不存在,具体参考api的注释
        LOG.info("查看键username:0001的存活时间, 剩余存活时间(seconds): {}", jedis.ttl("expireKey"));
    }

    @Test
    public void testPTTL() {
        jedis.del("expireKey");
        jedis.set("expireKey", "0001");
        LOG.info("设置key的超时时间, 执行操作的结果: {}", jedis.expire("expireKey", 10));

        LOG.info("设置key在某个时间点超时, 执行操作的结果: {}", jedis.expireAt("expireKey", Instant.now().getEpochSecond() + 8));

        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //pttl的单位是毫秒,精度更高precise
        LOG.info("查看键username:0001的存活时间, 剩余存活时间(seconds): {}", jedis.pttl("expireKey"));
    }
}
