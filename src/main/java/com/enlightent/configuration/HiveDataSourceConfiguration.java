package com.enlightent.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jianglei
 * @since 2018/1/30
 */

@Configuration
public class HiveDataSourceConfiguration {


    private String url;
    private String username;
    private String password;
    private Long connectionTimeout;
    private String driverClassName;
    private String connectionInitSql;

    @Bean
    @Qualifier("hiveHikariDataSource")
    public Object hiveHikariDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setConnectionTimeout(connectionTimeout);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setConnectionInitSql(connectionInitSql);
        dataSource.setMaximumPoolSize(10);
        dataSource.setMinimumIdle(1);

        return dataSource;
    }

    @Value("${hive.datasource.url}")
    public void setUrl(String url) {
        this.url = url;
    }

    @Value("${hive.datasource.username}")
    public void setUsername(String username) {
        this.username = username;
    }

    @Value("${hive.datasource.password}")
    public void setPassword(String password) {
        this.password = password;
    }

    @Value("${hive.datasource.connectionTimeout}")
    public void setConnectionTimeout(Long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    @Value("${hive.datasource.driver-class-name}")
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    @Value("${hive.datasource.connection-init-sql}")
    public void setConnectionInitSql(String connectionInitSql) {
        this.connectionInitSql = connectionInitSql;
    }
}
