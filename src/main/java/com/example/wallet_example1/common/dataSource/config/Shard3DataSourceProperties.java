package com.example.wallet_example1.common.dataSource.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@ConfigurationProperties(prefix = "spring.datasource.shard3")
@Getter @Setter
public class Shard3DataSourceProperties {
    private ClassLoader classLoader;
    private boolean generateUniqueName = true;
    private String name;
    private Class<? extends DataSource> type;
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private String jndiName;
    private EmbeddedDatabaseConnection embeddedDatabaseConnection;
    private DataSourceProperties.Xa xa = new DataSourceProperties.Xa();
    private String uniqueName;

    public Shard3DataSourceProperties() {
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void afterPropertiesSet() throws Exception {
        if (this.embeddedDatabaseConnection == null) {
            this.embeddedDatabaseConnection = EmbeddedDatabaseConnection.get(this.classLoader);
        }

    }

    public DataSourceBuilder<?> initializeDataSourceBuilder() {
        return DataSourceBuilder.create(this.getClassLoader()).type(this.getType()).driverClassName(this.determineDriverClassName()).url(this.determineUrl()).username(this.determineUsername()).password(this.determinePassword());
    }

    public String determineDriverClassName() {
        if (StringUtils.hasText(this.driverClassName)) {
            Assert.state(this.driverClassIsLoadable(), () -> {
                return "Cannot load driver class: " + this.driverClassName;
            });
            return this.driverClassName;
        } else {
            String driverClassName = null;
            if (StringUtils.hasText(this.url)) {
                driverClassName = DatabaseDriver.fromJdbcUrl(this.url).getDriverClassName();
            }

            if (!StringUtils.hasText(driverClassName)) {
                driverClassName = this.embeddedDatabaseConnection.getDriverClassName();
            }

            if (!StringUtils.hasText(driverClassName)) {
                throw new Shard3DataSourceProperties.DataSourceBeanCreationException("Failed to determine a suitable driver class", this, this.embeddedDatabaseConnection);
            } else {
                return driverClassName;
            }
        }
    }

    private boolean driverClassIsLoadable() {
        try {
            ClassUtils.forName(this.driverClassName, (ClassLoader)null);
            return true;
        } catch (UnsupportedClassVersionError var2) {
            throw var2;
        } catch (Throwable var3) {
            return false;
        }
    }

    public String determineUrl() {
        if (StringUtils.hasText(this.url)) {
            return this.url;
        } else {
            String databaseName = this.determineDatabaseName();
            String url = databaseName != null ? this.embeddedDatabaseConnection.getUrl(databaseName) : null;
            if (!StringUtils.hasText(url)) {
                throw new Shard3DataSourceProperties.DataSourceBeanCreationException("Failed to determine suitable jdbc url", this, this.embeddedDatabaseConnection);
            } else {
                return url;
            }
        }
    }

    public String determineDatabaseName() {
        if (this.generateUniqueName) {
            if (this.uniqueName == null) {
                this.uniqueName = UUID.randomUUID().toString();
            }

            return this.uniqueName;
        } else if (StringUtils.hasLength(this.name)) {
            return this.name;
        } else {
            return this.embeddedDatabaseConnection != EmbeddedDatabaseConnection.NONE ? "testdb" : null;
        }
    }

    public String determineUsername() {
        if (StringUtils.hasText(this.username)) {
            return this.username;
        } else {
            return EmbeddedDatabaseConnection.isEmbedded(this.determineDriverClassName(), this.determineUrl()) ? "sa" : null;
        }
    }
    public String determinePassword() {
        if (StringUtils.hasText(this.password)) {
            return this.password;
        } else {
            return EmbeddedDatabaseConnection.isEmbedded(this.determineDriverClassName(), this.determineUrl()) ? "" : null;
        }
    }

    public static class Xa {
        private String dataSourceClassName;
        private Map<String, String> properties = new LinkedHashMap();

        public Xa() {
        }

        public String getDataSourceClassName() {
            return this.dataSourceClassName;
        }

        public void setDataSourceClassName(String dataSourceClassName) {
            this.dataSourceClassName = dataSourceClassName;
        }

        public Map<String, String> getProperties() {
            return this.properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }
    }

    static class DataSourceBeanCreationException extends BeanCreationException {
        private final Shard3DataSourceProperties properties;
        private final EmbeddedDatabaseConnection connection;

        DataSourceBeanCreationException(String message, Shard3DataSourceProperties properties, EmbeddedDatabaseConnection connection) {
            super(message);
            this.properties = properties;
            this.connection = connection;
        }

        Shard3DataSourceProperties getProperties() {
            return this.properties;
        }

        EmbeddedDatabaseConnection getConnection() {
            return this.connection;
        }
    }
}
