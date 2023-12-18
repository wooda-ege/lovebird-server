package com.lovebird.api.dto.param.calendar

import com.lovebird.api.dto.request.calendar.CalendarUpdateRequest
import com.lovebird.domain.entity.User

data class CalendarUpdateParam(
	val calendarId: Long,
	val user: User,
	val request: CalendarUpdateRequest
)
