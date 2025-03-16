package com.example.wallet_example1.common.database;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

public class ShardRoutingDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();
    public static void setCurrentShard(String shardKey) {
        CONTEXT_HOLDER.set(shardKey);
    }
    public static void clear() {
        CONTEXT_HOLDER.remove();
    }
    @Override
    protected Object determineCurrentLookupKey() {
        return CONTEXT_HOLDER.get();
    }
}
