package com.lovebird.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor


@Configuration
@EnableScheduling
class ScheduleConfig {

	@Bean
	fun taskExecutor(): TaskExecutor {
		val threadPoolTaskExecutor = ThreadPoolTaskExecutor()

		threadPoolTaskExecutor.corePoolSize = 5
		threadPoolTaskExecutor.maxPoolSize = 10
		threadPoolTaskExecutor.queueCapacity = 25

		return threadPoolTaskExecutor
	}
}
