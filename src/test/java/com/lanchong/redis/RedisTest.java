package com.lanchong.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @program: SeckillProject
 * @description: redis 连接测试
 **/
public class RedisTest {
    @Test
    public void connect() {
        Jedis jedis = new Jedis("127.0.0.1",6379,1000);
        System.out.println(jedis.ping());
    }
}
