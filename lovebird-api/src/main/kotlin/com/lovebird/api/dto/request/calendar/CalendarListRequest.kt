package com.lovebird.api.dto.request.calendar

import com.lovebird.api.dto.param.calendar.CalendarListParam
import com.lovebird.domain.entity.User

data class CalendarListRequest(
	val year: Int?,
	val month: Int?
) {
	fun toParam(user: User): CalendarListParam {
		return CalendarListParam(year, month, user)
	}
}
