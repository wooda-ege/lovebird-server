package com.lovebird.domain.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Configuration
class DataSourceConfig {

	@Bean
	@ConfigurationProperties(prefix = "storage.datasource.core")
	fun hikariConfig(): HikariConfig = HikariConfig()

	@Bean
	@Primary
	fun dataSource(): DataSource = HikariDataSource(hikariConfig())
}
