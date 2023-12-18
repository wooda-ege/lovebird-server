package com.lovebird.api.dto.request.calendar

import com.lovebird.common.enums.Alarm
import com.lovebird.common.enums.Color
import com.lovebird.domain.entity.Calendar
import com.lovebird.domain.entity.User
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalTime

data class CalendarUpdateRequest(
	val title: String,
	val memo: String?,
	val color: Color,
	val alarm: Alarm,
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	val startDate: LocalDate,
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	val endDate: LocalDate,
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	val startTime: LocalTime?,
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	val endTime: LocalTime?
) {
	fun toEntity(user: User): Calendar {
		return Calendar(title, memo, startDate, endDate, startTime, endTime, color, alarm, user)
	}
}
