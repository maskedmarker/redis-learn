package org.example.learn.redis.jedis.standlone.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Set;

public class SetApiTest {

    private static final Logger LOG = LoggerFactory.getLogger(SetApiTest.class);

    private static final String HOST = "192.168.56.101";
    private static final int PORT = 6379;
    private static final String KEY1 = "test:set:1";
    private static final String KEY2 = "test:set:2";
    private static final String VALUE = "changjiang";
    private Jedis jedis;




    @Before
    public void setupJedis() {
        jedis = new Jedis(HOST, PORT);
    }

    @After
    public void destoryData() {
        jedis.del(KEY1);
        jedis.del(KEY2);
    }


    // ********************************增删查操作************************************************
    @Test
    public void testAddAndDel() {
        // set的增加操作. 因为不包含位置信息,所以用add这个单词?
        jedis.sadd(KEY1, VALUE + "001", VALUE + "002", VALUE + "003");
        LOG.info("当前KEY1中的全量元素: {}", jedis.smembers(KEY1));

        // 删除操作
        jedis.srem(KEY1, VALUE + "002");
        LOG.info("当前KEY1中的全量元素: {}", jedis.smembers(KEY1));

        // 随机删除元素
        jedis.sadd(KEY1, VALUE + "011", VALUE + "012", VALUE + "013");
        Set<String> popedElements = jedis.spop(KEY1, 2);
        LOG.info("KEY1中被随机删除的N个元素为: {}", popedElements);

        // 将指定值的元素从一个set中删除,并将删除的元素转移到另一个set中
        jedis.sadd(KEY1, VALUE + "004");
        jedis.smove(KEY1, KEY2, VALUE + "004");
        LOG.info("当前KEY1中的全量元素: {}", jedis.smembers(KEY1));
        LOG.info("当前KEY2中的全量元素: {}", jedis.smembers(KEY2));

    }

    @Test
    public void testQuery() {
        jedis.sadd(KEY1, VALUE + "001", VALUE + "002", VALUE + "003");
        jedis.sadd(KEY1, VALUE + "001", VALUE + "002", VALUE + "003");
        jedis.sadd(KEY1, VALUE + "001", VALUE + "002", VALUE + "003");
        jedis.sadd(KEY1, VALUE + "011", VALUE + "012", VALUE + "013");

        // 查询指定值是否再set中
        LOG.info("VALUE003是否在set中: {}", jedis.sismember(KEY1, VALUE + "003"));

        // 从set中随机选出N个元素,非删除
        LOG.info("随机选取N个元素: {}", jedis.srandmember(KEY1));  //不带count参数时,等于count=1
        LOG.info("随机选取N个元素: {}", jedis.srandmember(KEY1, 2));

        // 当count>set的元素个数时,还可以保证元素不重复,当count<-set的元素个数时,不能保证元素不重复,强行凑够N个元素
        LOG.info("随机选取N个元素: {}", jedis.srandmember(KEY1, 10));
        LOG.info("随机选取N个元素: {}", jedis.srandmember(KEY1, -10));


        // 查询set的元素个数
        LOG.info("set的元素个数(cardinality 基势): {}", jedis.scard(KEY1));

        // 通过集合操作来查询
        jedis.del(KEY1);
        jedis.del(KEY2);
        jedis.sadd(KEY1, VALUE + "001", VALUE + "002", VALUE + "003");
        jedis.sadd(KEY2, VALUE + "011", VALUE + "012", VALUE + "003");

        LOG.info("通过集合的union操作,来查询元素: {}", jedis.sunion(KEY1, KEY2));
        LOG.info("通过集合的intersection操作,来查询元素: {}", jedis.sinter(KEY1, KEY2));
        LOG.info("通过集合的diff操作,来查询元素: {}", jedis.sdiff(KEY1, KEY2));
    }


    @Test
    public void testX() {


    }


}
