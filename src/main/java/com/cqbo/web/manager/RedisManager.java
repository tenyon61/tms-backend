package com.cqbo.web.manager;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * RedisManager
 *
 * @author tenyon
 * @date 2024/11/22
 */
@Component
public class RedisManager<V> {
    private static final Logger logger = LoggerFactory.getLogger(RedisManager.class);

    @Resource
    private RedisTemplate<String, V> redisTemplate;

    /**
     * 普通缓存放入并设置时间
     *
     * @param key
     * @param value
     * @param expired  ms
     * @return
     */
    public boolean setex(String key, V value, long expired) {
        try {
            if (expired > 0) {
                redisTemplate.opsForValue().set(key, value, expired, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            logger.error("设置redisKey:{},value:{}失败", key, value);
            return false;
        }
    }

    /**
     * 普通缓存放入
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, V value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            logger.error("设置redisKey:{},value:{}失败", key, value);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 从列表中移除指定的值，移除的数量为 1
     *
     * @param key
     * @param value
     * @return
     */
    public long remove(String key, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, 1, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据 key获取 value
     *
     * @param key
     * @return
     */
    public V get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取列表类型键的所有元素
     *
     * @param key
     * @return
     */
    public List<V> getQueueList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 将一个值插入到列表的左边（头部）。如果指定了过期时间，则设置过期时间
     *
     * @param key
     * @param value
     * @param expired s
     * @return
     */
    public boolean lpush(String key, V value, Long expired) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            if (expired != null && expired > 0) {
                expire(key, expired);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将多个值插入到列表的左边（头部）。如果指定了过期时间，则设置过期时间
     *
     * @param key
     * @param values
     * @param expired s
     * @return
     */
    public boolean lpushAll(String key, List<V> values, long expired) {
        try {
            redisTemplate.opsForList().leftPushAll(key, values);
            if (expired > 0) {
                expire(key, expired);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从列表的右边（尾部）弹出一个值
     *
     * @param key
     * @return
     */
    public V rpop(String key) {
        try {
            return redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * key是否存在
     *
     * @param key
     * @return
     */
    public boolean keyExists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 根据键前缀获取所有匹配的键
     *
     * @param keyPrifix
     * @return
     */
    public Set<String> getByKeyPrefix(String keyPrifix) {
        Set<String> keyList = redisTemplate.keys(keyPrifix + "*");
        return keyList;
    }

    /**
     * 根据键前缀批量获取键值对
     *
     * @param keyPrifix
     * @return
     */
    public Map<String, V> getBatch(String keyPrifix) {
        Set<String> keySet = redisTemplate.keys(keyPrifix + "*");
        List<String> keyList = new ArrayList<>(keySet);
        List<V> keyValueList = redisTemplate.opsForValue().multiGet(keyList);
        Map<String, V> resultMap = keyList.stream().collect(Collectors.toMap(key -> key, value -> keyValueList.get(keyList.indexOf(value))));
        return resultMap;
    }

    /**
     * 为指定的键设置过期时间（以毫秒为单位）
     *
     * @param key
     * @param expired s
     * @return
     */
    public boolean expire(String key, long expired) {
        try {
            if (expired > 0) {
                redisTemplate.expire(key, expired, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}