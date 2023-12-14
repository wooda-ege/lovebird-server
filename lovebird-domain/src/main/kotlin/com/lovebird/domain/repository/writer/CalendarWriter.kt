package com.lovebird.domain.repository.writer

import com.lovebird.domain.annotation.Writer
import com.lovebird.domain.repository.jpa.CalendarJpaRepository

@Writer
class CalendarWriter(
	private val calendarJpaRepository: CalendarJpaRepository
)
