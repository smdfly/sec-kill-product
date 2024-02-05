package com.lanchong.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

/**
 * @program: SeckillProject
 * @description: redis服务
 **/
@Service
public class RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 设置对象
     *
     * @param prefix 对象Prefix
     * @param key    键
     * @param value  值
     * @param exTime 过期时间
     * @param <T>    返回类型
     * @return
     */
    public <T> boolean set(KeyPrefix prefix, String key, T value, int exTime) {
        String str = beanToString(value);
        if (str == null || str.length() <= 0) {
            return false;
        }
        //生成唯一key
        String realKey = prefix.getPrefix() + key;
        //设置过期时间
        if (exTime <= 0) {
            stringRedisTemplate.opsForValue().set(realKey, str);
        } else {
            return stringRedisTemplate.opsForValue().setIfAbsent(realKey, str, exTime, TimeUnit.SECONDS);
        }
        return true;
    }

    /**
     * 根据key获取对象返回
     *
     * @param prefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        String realKey = prefix.getPrefix() + key;
        String str = stringRedisTemplate.opsForValue().get(realKey);
        T t = stringToBean(str, clazz);
        return t;
    }

    /**
     * bean 转 String
     *
     * @param value
     * @param <T>
     * @return
     */
    public static <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else {
            return JSON.toJSONString(value);
        }
    }


    /**
     * string转bean
     *
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T stringToBean(String str, Class<T> clazz) {
        if (str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    /**
     * 判断key是否存在
     */
    public <T> boolean exists(KeyPrefix prefix, String key) {
        //生成真正的key
        String realKey  = prefix.getPrefix() + key;
        return stringRedisTemplate.hasKey(realKey);
    }

    /**
     * 增加值
     */
    public <T> Long incr(KeyPrefix prefix, String key) {
        //生成真正的key
        String realKey  = prefix.getPrefix() + key;
        return stringRedisTemplate.opsForValue().increment(realKey);
    }

    /**
     * 减少值
     */
    public <T> Long decr(KeyPrefix prefix, String key) {
        //生成真正的key
        String realKey  = prefix.getPrefix() + key;
        return stringRedisTemplate.opsForValue().decrement(realKey);
    }
}
