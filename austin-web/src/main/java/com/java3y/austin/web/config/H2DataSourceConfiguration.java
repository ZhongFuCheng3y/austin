package com.java3y.austin.web.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


/**
 * h2 配置类
 */
@Configuration
public class H2DataSourceConfiguration {
	
	private String driver = "org.h2.Driver";
	private String url = "jdbc:h2:mem:austin;DB_CLOSE_DELAY=-1";
	private String user = "";
	private String password = "";
	
	
	@Bean
	@ConditionalOnProperty(value = "austin.database.datasource", havingValue = "h2", matchIfMissing = true)
	public DataSource dataSource() {
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName(driver);
		dataSourceBuilder.url(url);
		dataSourceBuilder.username(user);
		dataSourceBuilder.password(password);
		return dataSourceBuilder.build();
	}
	
}
