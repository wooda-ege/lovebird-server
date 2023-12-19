package com.lovebird.domain.repository.reader

import com.lovebird.domain.annotation.Reader
import com.lovebird.domain.entity.Calendar
import com.lovebird.domain.entity.CalendarEvent
import com.lovebird.domain.repository.jpa.CalendarEventJpaRepository

@Reader
class CalendarEventReader(
	private val calendarEventJpaRepository: CalendarEventJpaRepository
) {
	fun findCalendarEventsByCalendar(calendar: Calendar): List<CalendarEvent> {
		return calendarEventJpaRepository.findAllByCalendar(calendar)
	}
}
