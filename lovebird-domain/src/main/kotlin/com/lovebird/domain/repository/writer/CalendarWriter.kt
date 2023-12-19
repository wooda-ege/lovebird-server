package com.lovebird.domain.repository.writer

import com.lovebird.domain.annotation.Writer
import com.lovebird.domain.entity.Calendar
import com.lovebird.domain.repository.jpa.CalendarJpaRepository

@Writer
class CalendarWriter(
	private val calendarJpaRepository: CalendarJpaRepository
) {
	fun save(calendar: Calendar): Calendar {
		return calendarJpaRepository.save(calendar)
	}
}
