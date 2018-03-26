package com.enlightent.configuration;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author 作者 wr:
 * @version 创建时间：2018年2月8日 下午12:02:15 类说明
 */
@Configuration
public class DataSourceConfig {
	
	@Bean(name = "devDataSource")
	@Qualifier("devDataSource")
	@ConfigurationProperties(prefix = "spring.dev.datasource")
	public DataSource devDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "primaryDataSource")
	@Qualifier("primaryDataSource")
	@ConfigurationProperties(prefix = "spring.datasource")
	@Primary
	public DataSource primaryDataSource() {
		return DataSourceBuilder.create().build();
	}
}
