package com.example.wallet_example1.common.config;

import com.example.wallet_example1.common.database.ShardRoutingDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource shard1DataSource() {
        return createHikariDataSource("jdbc:mysql://localhost:3307/app", "root", "1234");
    }

    @Bean
    public DataSource shard2DataSource() {
        return createHikariDataSource("jdbc:mysql://localhost:3308/app", "root", "1234");
    }

    @Bean
    public DataSource shardRoutingDataSource(
            @Qualifier("shard1DataSource") DataSource shard1,
            @Qualifier("shard2DataSource") DataSource shard2) {

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("shardA", shard1);
        targetDataSources.put("shardB", shard2);

        ShardRoutingDataSource routingDataSource = new ShardRoutingDataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(shard1); // 기본 데이터소스 설정
        routingDataSource.afterPropertiesSet();
        return routingDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("shardRoutingDataSource") DataSource shardRoutingDataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(shardRoutingDataSource);
        factoryBean.setPackagesToScan("com.example.wallet_example1.member.infrastructure.entity");

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        jpaProperties.put("hibernate.show_sql", true);
        jpaProperties.put("hibernate.format_sql", true);
        jpaProperties.put("spring.jpa.hibernate.ddl-auto", "create");
        factoryBean.setJpaProperties(jpaProperties);

        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return factoryBean;
    }

    private DataSource createHikariDataSource(String url, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(30);
        config.setMinimumIdle(10);

        return new HikariDataSource(config);
    }
}
