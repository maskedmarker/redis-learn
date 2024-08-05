package org.example.learn.redis.jedis.standlone.api;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.List;

public class ListApiTest {

    private static final Logger LOG = LoggerFactory.getLogger(StringApiTest.class);

    private static final String HOST = "192.168.56.101";
    private static final int PORT = 6379;
    private static final String KEY = "test:list";
    private static final String VALUE = "changjiang";
    private Jedis jedis;




    @Before
    public void setupJedis() {
        jedis = new Jedis(HOST, PORT);
    }


    // ********************************增删查操作************************************************
    @Test
    public void testAddAndDel() {
        jedis.del(KEY);

        // 增加操作发生在两端
        jedis.lpush(KEY, VALUE + "001");
        jedis.rpush(KEY, VALUE + "002");
        jedis.rpush(KEY, VALUE + "003", VALUE + "004", VALUE + "005");

        List<String> lRange = jedis.lrange(KEY, 0, -1);
        LOG.info("lRange = {}", lRange);

        // 在指定位置(0-based)插入元素
        jedis.lset(KEY, 3, VALUE + "0031");
        LOG.info("当前list的全量元素: {}", jedis.lrange(KEY, 0, -1));

        // 在两端删除
        jedis.lpop(KEY);
        jedis.rpop(KEY);

        lRange = jedis.lrange(KEY, 0, -1);
        LOG.info("lRange = {}", lRange);

        // 无法直接删除指定位置的元素
        // 删除特定值的n个元素
        jedis.del(KEY);
        jedis.rpush(KEY, VALUE + "001", VALUE + "002", VALUE + "003");
        jedis.rpush(KEY, VALUE + "001", VALUE + "002", VALUE + "003");
        jedis.rpush(KEY, VALUE + "001", VALUE + "002", VALUE + "003");
        jedis.rpush(KEY, VALUE + "001", VALUE + "002", VALUE + "003");

        // 从左到右,删除2个元素
        jedis.lrem(KEY, 2, VALUE + "002");
        LOG.info("当前list的全量元素: {}", jedis.lrange(KEY, 0, -1));

        // 从右到左,删除2个元素
        jedis.lrem(KEY, -2, VALUE + "001");
        LOG.info("当前list的全量元素: {}", jedis.lrange(KEY, 0, -1));

    }

    @Test
    public void testQuery() {
        jedis.del(KEY);
        jedis.rpush(KEY, VALUE + "001", VALUE + "002", VALUE + "003", VALUE + "004", VALUE + "005");
        jedis.rpush(KEY, VALUE + "001", VALUE + "002", VALUE + "003");

        //查出指定位置的元素值
        LOG.info("索引位是2的元素: {}", jedis.lindex(KEY, 2));

        //查找元素值等于特定值的元素的元素位置(redis.version >=6才有,jedis还没有开放接口)
        //LOG.info("元素值等于2的元素的索引: {}", jedis.lpos(KEY));

        //查询list的元素个数
        LOG.info("list元素的总个数: {}", jedis.llen(KEY));
    }


    /**
     * String的NX操作和List的X操作,都是以key是否存在为判断依据.
     */
    @Test
    public void testX() {
        jedis.del(KEY);

        //当list存在时,插入元素的操作才会成功.操作的返回值是list最新的长度
        LOG.info("操作是否成功: {}", jedis.rpushx(KEY, VALUE + "001"));
        LOG.info("操作是否成功: {}", jedis.rpushx(KEY, VALUE + "002"));
        LOG.info("操作是否成功: {}", jedis.rpushx(KEY, VALUE + "003"));

        //不存在list为空的情况,为空时,逻辑上可以认为该list已经不存在了
        jedis.rpush(KEY, "000");
        jedis.rpop(KEY);

        LOG.info("操作是否成功: {}", jedis.rpushx(KEY, VALUE + "001"));

        jedis.rpush(KEY, "000");
        LOG.info("操作是否成功: {}", jedis.rpushx(KEY, VALUE + "001"));


    }


}
