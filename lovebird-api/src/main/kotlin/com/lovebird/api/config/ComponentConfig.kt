package com.lovebird.api.config

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EntityScan("com.lovebird.domain.entity.**")
@EnableJpaRepositories("com.lovebird.domain.repository.**")
@ComponentScan(basePackages = ["com.lovebird"])
class ComponentConfig
