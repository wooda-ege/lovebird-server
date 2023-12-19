package com.lovebird.domain.repository.jpa

import com.lovebird.domain.entity.Calendar
import com.lovebird.domain.entity.CalendarEvent
import org.springframework.data.jpa.repository.JpaRepository

interface CalendarEventJpaRepository : JpaRepository<CalendarEvent, Long> {

	fun findAllByCalendar(calendar: Calendar): List<CalendarEvent>
}
