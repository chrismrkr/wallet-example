package com.example.wallet_example1.common.transactionManager.shard1;

import com.example.wallet_example1.common.dataSource.config.Shard1DataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableConfigurationProperties(Shard1DataSourceProperties.class)
@Slf4j
public class Shard1DataSourcePropertiesTest {
    @Autowired
    Shard1DataSourceProperties dataSourceProperties;

    @Test
    void 프로퍼티_확인() {
        log.info("start");
        Assertions.assertNotNull(dataSourceProperties.getUsername());
        Assertions.assertNotNull(dataSourceProperties.getPassword());
    }
}
