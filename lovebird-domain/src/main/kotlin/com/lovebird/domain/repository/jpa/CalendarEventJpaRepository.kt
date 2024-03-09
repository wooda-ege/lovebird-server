package com.lovebird.domain.repository.jpa

import com.lovebird.domain.entity.Calendar
import com.lovebird.domain.entity.CalendarEvent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CalendarEventJpaRepository : JpaRepository<CalendarEvent, Long> {

	fun findAllByCalendar(calendar: Calendar): List<CalendarEvent>

	fun deleteAllByCalendarIn(calendars: List<Calendar>)
}
