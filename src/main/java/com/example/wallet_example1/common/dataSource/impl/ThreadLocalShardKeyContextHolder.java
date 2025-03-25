package com.example.wallet_example1.common.dataSource.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

@Slf4j
public class ThreadLocalShardKeyContextHolder {
    private static ThreadLocal<DBShardKey> CONTEXT = new ThreadLocal<>();
    private static final long shardNum = 3L;
    public static DBShardKey getShardKey() {
        return CONTEXT.get();
    }
    public static void setKey(long id) {
        int idx = (int) (id % shardNum);
        DBShardKey KEY = DBShardKey.values()[idx];
        log.info("Shard Key Selected: {} -> {}", id, KEY.name());
        CONTEXT.set(KEY);
    }

    public static void setKey(DBShardKey key) {
        Assert.notNull(key, "KEY cannot be null");
        CONTEXT.set(key);
    }
    public static void clear() {
        log.info("Thread Local Free: {}", CONTEXT.get().name());
        CONTEXT.remove();
    }
}
