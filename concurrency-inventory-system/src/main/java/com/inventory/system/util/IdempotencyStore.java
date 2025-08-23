package com.inventory.system.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class IdempotencyStore {
    private final StringRedisTemplate redis;
    public IdempotencyStore(StringRedisTemplate redis) { this.redis = redis; }

    /** @return true if this key was newly recorded (i.e., first time), false if already processed */
    public boolean firstTime(String key, Duration ttl) {
        Boolean set = redis.opsForValue().setIfAbsent("idem:" + key, "1", ttl);
        return Boolean.TRUE.equals(set);
    }
}
