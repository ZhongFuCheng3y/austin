package com.java3y.austin.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * mysql 配置类
 */
@Configuration
public class MySQLDataSourceConfiguration {
	@Value("${spring.datasource.driver-class-name}")
	private String driver;
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String user;
	@Value("${spring.datasource.password}")
	private String password;
	
	@Value("${austin.database.schema}")
	private String schema;	

	
	
	@Bean
	@ConditionalOnProperty(value = "austin.database.datasource", havingValue = "mysql")
	public DataSource dataSource() {
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName(driver);
		dataSourceBuilder.url(url);
		dataSourceBuilder.username(user);
		dataSourceBuilder.password(password);
		DataSource dataSource = dataSourceBuilder.build();
		createSchema(dataSource);
		return dataSource;
	}
	
	private void createSchema(DataSource dataSource) {
		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url.substring(0, url.indexOf("?")).replace(schema, ""), user, password);
			Statement statement = conn.createStatement();
			statement.executeUpdate("create database if not exists `" + schema + "` default character set utf8 COLLATE utf8_general_ci");
			statement.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
