package com.lovebird.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QuerydslConfig {
	@PersistenceContext
	lateinit var entityManager: EntityManager

	@Bean
	fun queryFactory(): JPAQueryFactory {
		return JPAQueryFactory(entityManager)
	}
}
