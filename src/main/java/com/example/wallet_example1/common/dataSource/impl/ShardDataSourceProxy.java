package com.example.wallet_example1.common.dataSource.impl;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class ShardDataSourceProxy extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        DBShardKey shardKey = ThreadLocalShardKeyContextHolder.getShardKey();
        return shardKey;
    }
}
