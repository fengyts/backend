package com.backend.redis;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

@Component
@Slf4j
public class RedisLockUtil {

    private static final Long RELEASE_SUCCESS = 1L;
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "EX";
    private static final String RELEASE_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * set方式
     * @param lockKey
     * @param requestId
     * @param expireTime
     * @return
     */
    public boolean getLock(String lockKey, String requestId, int expireTime) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, requestId, expireTime, TimeUnit.SECONDS);
        return result;
    }

    public boolean tryLock(String lockKey, String requestId, int expireTime) {
        RedisCallback<Boolean> callback = redisConnection -> {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            // jedis旧版本实现方式
//            String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
            // jedis新版本实现方式
            SetParams setParams = new SetParams();
            setParams.nx().ex(expireTime);
            String result = jedis.set(lockKey, requestId, setParams);
            return LOCK_SUCCESS.equals(result);
        };
        Object res = redisTemplate.execute(callback);
        return (Boolean) res;
    }

    public boolean tryLock1(String lockKey, String requestId, long expire, TimeUnit timeUnit) {
        try {
            RedisCallback<Boolean> callback = (connection) -> {
                Boolean set = connection.set(lockKey.getBytes(Charset.forName("UTF-8")),
                        requestId.getBytes(Charset.forName("UTF-8")),
                        Expiration.seconds(timeUnit.toSeconds(expire)),
                        RedisStringCommands.SetOption.SET_IF_ABSENT);
                return set;
            };
            return (boolean) redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("redis lock error.", e);
        }
        return false;
    }

    public boolean releaseLock(String lockKey, String requestId){
        Object res = redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            Object result = jedis.eval(RELEASE_LOCK_SCRIPT, Collections.singletonList(lockKey),
                    Collections.singletonList(requestId));
            return RELEASE_SUCCESS.equals(result);
        });
        return (boolean) res;
    }


}
