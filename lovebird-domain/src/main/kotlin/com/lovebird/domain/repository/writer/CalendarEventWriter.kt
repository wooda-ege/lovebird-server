package com.lovebird.domain.repository.writer

import com.lovebird.domain.annotation.Writer
import com.lovebird.domain.dto.query.CalendarEventRequestParam
import com.lovebird.domain.entity.CalendarEvent
import com.lovebird.domain.repository.jpa.CalendarEventJpaRepository

@Writer
class CalendarEventWriter(
	private val calendarEventJpaRepository: CalendarEventJpaRepository
) {

	fun save(calendarEventCreateParam: CalendarEventRequestParam) {
		calendarEventJpaRepository.save(calendarEventCreateParam.toEntity())
	}

	fun deleteAll(calendarEvents: List<CalendarEvent>) {
		calendarEventJpaRepository.deleteAll(calendarEvents)
	}
}
