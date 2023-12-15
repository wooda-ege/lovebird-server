package com.lovebird.api.dto.response.calendar

import com.lovebird.domain.entity.Calendar
import java.time.LocalDate
import java.time.LocalTime

data class CalendarDetailResponse(
	val id: Long,
	val userId: Long,
	val title: String,
	val memo: String? = null,
	val color: String,
	val alarm: String,
	val statDate: LocalDate,
	val endDate: LocalDate? = null,
	val startTime: LocalTime? = null,
	val endTime: LocalTime? = null
) {
	companion object {
		fun of(calendar: Calendar): CalendarDetailResponse {
			return CalendarDetailResponse(
				calendar.id!!,
				calendar.user.id!!,
				calendar.title,
				calendar.memo,
				calendar.color.toString(),
				calendar.alarm.toString(),
				calendar.startDate,
				calendar.endDate,
				calendar.startTime,
				calendar.endTime
			)
		}
	}
}
