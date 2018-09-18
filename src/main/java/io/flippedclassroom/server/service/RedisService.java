package io.flippedclassroom.server.service;

import java.util.concurrent.TimeUnit;

/**
 * 利用 Redis 缓存 Token
 */
public interface RedisService {
    void save(String key, String value);

    void saveWithExpire(String key, String value, long timeout, TimeUnit timeUnit);

    boolean setExpire(String key, long timeout, TimeUnit timeUnit);

    String get(String key);

    void delete(String key);
}
