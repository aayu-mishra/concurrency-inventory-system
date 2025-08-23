package com.inventory.system.util;

import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisLockManager {
    private final StringRedisTemplate redis;

    public RedisLockManager(StringRedisTemplate redis) { this.redis = redis; }

    /** Try to acquire a lock for `key` with value `token` and ttlSeconds. */
    public boolean tryLock(String key, String token, long ttlSeconds) {
        Boolean ok = redis.opsForValue().setIfAbsent(key, token, Duration.ofSeconds(ttlSeconds));
        return Boolean.TRUE.equals(ok);
    }

    /** Release lock only if the token matches (atomic via Lua). */
    public void unlock(String key, String token) {
        String lua =
                "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                        "  return redis.call('del', KEYS[1]) else return 0 end";
        redis.execute((RedisCallback<Object>) (con) -> con.scriptingCommands().eval(
                lua.getBytes(), ReturnType.INTEGER, 1, key.getBytes(), token.getBytes()));
    }
}

