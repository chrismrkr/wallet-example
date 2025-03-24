package com.example.wallet_example1.common.dataSource.config;

import com.example.wallet_example1.common.dataSource.impl.DBShardKey;
import com.example.wallet_example1.common.dataSource.impl.ShardDataSourceProxy;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties({Shard1DataSourceProperties.class, Shard2DataSourceProperties.class, Shard3DataSourceProperties.class})
public class DataSourceConfiguration {
    private final Shard1DataSourceProperties shard1DataSourceProperties;
    private final Shard2DataSourceProperties shard2DataSourceProperties;
    private final Shard3DataSourceProperties shard3DataSourceProperties;
    public DataSourceConfiguration(Shard1DataSourceProperties shard1DataSourceProperties, Shard2DataSourceProperties shard2DataSourceProperties, Shard3DataSourceProperties shard3DataSourceProperties) {
        this.shard1DataSourceProperties = shard1DataSourceProperties;
        this.shard2DataSourceProperties = shard2DataSourceProperties;
        this.shard3DataSourceProperties = shard3DataSourceProperties;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        ShardDataSourceProxy shardDataSourceProxy = new ShardDataSourceProxy();
        shardDataSourceProxy.setTargetDataSources(createDataSourceMap());
        shardDataSourceProxy.setDefaultTargetDataSource(createDefaultDataSource());
        return shardDataSourceProxy;
    }
    private Map<Object, Object> createDataSourceMap() {
        Map<Object, Object> dataSourceMap = new HashMap<>();
        DataSource dataSource1 = createShard1HikariDataSource();
        DataSource dataSource2 = createShard2HikariDataSource();
        DataSource dataSource3 = createShard3HikariDataSource();
        dataSourceMap.put(DBShardKey.SHARD1, dataSource1);
        dataSourceMap.put(DBShardKey.SHARD2, dataSource2);
        dataSourceMap.put(DBShardKey.SHARD3, dataSource3);
        return dataSourceMap;
    }

    private DataSource createDefaultDataSource() {
        return createShard1HikariDataSource();
    }



    private DataSource createShard1HikariDataSource() {
        HikariDataSource dataSource = (HikariDataSource) createShard1DataSource(HikariDataSource.class, shard1DataSourceProperties);
        if (StringUtils.hasText(shard1DataSourceProperties.getName())) {
            dataSource.setPoolName(shard1DataSourceProperties.getName());
        }
        return dataSource;
    }

    private DataSource createShard1DataSource(Class<? extends DataSource> type, Shard1DataSourceProperties dataSourceProperties) {
        return DataSourceBuilder
                .create(dataSourceProperties.getClassLoader())
                .type(type)
                .driverClassName(dataSourceProperties.getDriverClassName())
                .url(dataSourceProperties.getUrl())
                .username(dataSourceProperties.getUsername())
                .password(dataSourceProperties.getPassword())
                .build();
    }

    private DataSource createShard2HikariDataSource() {
        HikariDataSource dataSource = (HikariDataSource) createShard2DataSource(HikariDataSource.class, shard2DataSourceProperties);
        if (StringUtils.hasText(shard2DataSourceProperties.getName())) {
            dataSource.setPoolName(shard2DataSourceProperties.getName());
        }
        return dataSource;
    }

    private DataSource createShard2DataSource(Class<? extends DataSource> type, Shard2DataSourceProperties dataSourceProperties) {
        return DataSourceBuilder
                .create(dataSourceProperties.getClassLoader())
                .type(type)
                .driverClassName(dataSourceProperties.getDriverClassName())
                .url(dataSourceProperties.getUrl())
                .username(dataSourceProperties.getUsername())
                .password(dataSourceProperties.getPassword())
                .build();
    }

    private DataSource createShard3HikariDataSource() {
        HikariDataSource dataSource = (HikariDataSource) createShard3DataSource(HikariDataSource.class, shard3DataSourceProperties);
        if (StringUtils.hasText(shard3DataSourceProperties.getName())) {
            dataSource.setPoolName(shard3DataSourceProperties.getName());
        }
        return dataSource;
    }

    private DataSource createShard3DataSource(Class<? extends DataSource> type, Shard3DataSourceProperties dataSourceProperties) {
        return DataSourceBuilder
                .create(dataSourceProperties.getClassLoader())
                .type(type)
                .driverClassName(dataSourceProperties.getDriverClassName())
                .url(dataSourceProperties.getUrl())
                .username(dataSourceProperties.getUsername())
                .password(dataSourceProperties.getPassword())
                .build();
    }
}
