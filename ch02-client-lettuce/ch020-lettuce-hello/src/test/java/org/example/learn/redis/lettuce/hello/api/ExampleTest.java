package org.example.learn.redis.lettuce.hello.api;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import org.example.learn.redis.lettuce.hello.constant.RedisConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleTest {

    private static final Logger logger = LoggerFactory.getLogger(ExampleTest.class);

    private static final String KEY = "test:key";
    private static final String VALUE = "changjiang";

    private RedisClient client;
    StatefulRedisConnection<String, String> connection;
    // RedisCommands类似于Jedis,都是用来发送redis命令的
    RedisCommands<String, String> syncCommands;
    RedisAsyncCommands<String, String> asyncCommands;

    @Before
    public void setup() {
        String redisUri = String.format("redis://%s:%s", RedisConstants.HOST, RedisConstants.PORT);

        // Create RedisClient and establish a connection
        client = RedisClient.create(redisUri);
        connection = client.connect();
        // Get sync API and perform Redis operations
        syncCommands = connection.sync();
        asyncCommands = connection.async();
    }

    @After
    public void destroy() {
        // Close the connection and client
        connection.close();
        client.shutdown();
    }

    /**
     * 同步命令
     */
    @Test
    public void syncCmd() {
        syncCommands.set("name", "Lettuce");
        String value = syncCommands.get("name");

        logger.info("Value from Redis: {}", value);
    }

    /**
     * 异步命令
     */
    @Test
    public void asyncCmd() {
        asyncCommands.set("name", "LettuceAsync");
        asyncCommands.get("name").thenAccept(value -> {
            logger.info("Value from Redis: {}", value);
        });
    }
}
