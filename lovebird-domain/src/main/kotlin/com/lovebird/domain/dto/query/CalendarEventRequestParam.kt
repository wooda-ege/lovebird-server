package com.lovebird.domain.dto.query

import com.lovebird.domain.entity.Calendar
import com.lovebird.domain.entity.CalendarEvent
import com.lovebird.domain.entity.User
import java.time.LocalDateTime

data class CalendarEventRequestParam(
	val calendar: Calendar,
	val user: User,
	val eventAt: LocalDateTime
) {
	fun toEntity(): CalendarEvent {
		return CalendarEvent(calendar, user, eventAt, calendar.alarm!!)
	}
}
