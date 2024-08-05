package org.example.learn.redis.jedis.hello.pipeline;


import org.example.learn.redis.jedis.hello.constant.RedisConstants;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PipelineTest {
    private static final Jedis jedis;

    static {
        jedis = new Jedis(RedisConstants.HOST, RedisConstants.PORT);
    }

    public static void main(String[] args) {
        pipelineResponse();
    }

    public static void pipelineResponse() {
        jedis.set("string", "foo");
        jedis.lpush("list", "foo");
        jedis.hset("hash", "foo", "bar");
        jedis.zadd("zset", 1, "foo");
        jedis.sadd("set", "foo");
        jedis.setrange("setrange", 0, "0123456789");
        byte[] bytesForSetRange = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        jedis.setrange("setrangebytes".getBytes(), 0, bytesForSetRange);

        Pipeline p = jedis.pipelined();
        Response<String> string = p.get("string");
        Response<String> list = p.lpop("list");
        Response<String> hash = p.hget("hash", "foo");
        Response<Set<String>> zset = p.zrange("zset", 0, -1);
        Response<String> set = p.spop("set");
        Response<Boolean> blist = p.exists("list");
        Response<Double> zincrby = p.zincrby("zset", 1, "foo");
        Response<Long> zcard = p.zcard("zset");
        p.lpush("list", "bar");
        Response<List<String>> lrange = p.lrange("list", 0, -1);
        Response<Map<String, String>> hgetAll = p.hgetAll("hash");
        p.sadd("set", "foo");
        Response<Set<String>> smembers = p.smembers("set");
        Response<Set<Tuple>> zrangeWithScores = p.zrangeWithScores("zset", 0, -1);
        Response<String> getrange = p.getrange("setrange", 1, 3);
        Response<byte[]> getrangeBytes = p.getrange("setrangebytes".getBytes(), 6, 8);
        p.sync();

        assertEquals("foo", string.get());
        assertEquals("foo", list.get());
        assertEquals("bar", hash.get());
        assertEquals("foo", zset.get().iterator().next());
        assertEquals("foo", set.get());
        assertEquals(false, blist.get());
        assertEquals(Double.valueOf(2), zincrby.get());
        assertEquals(Long.valueOf(1), zcard.get());
        assertEquals(1, lrange.get().size());
        assertNotNull(hgetAll.get().get("foo"));
        assertEquals(1, smembers.get().size());
        assertEquals(1, zrangeWithScores.get().size());
        assertEquals("123", getrange.get());
        byte[] expectedGetRangeBytes = { 6, 7, 8 };
        assertArrayEquals(expectedGetRangeBytes, getrangeBytes.get());
    }
}
