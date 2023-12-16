package com.lovebird.api.dto.request.calendar

import com.lovebird.api.dto.param.calendar.CalendarListParam
import com.lovebird.domain.entity.User
import jakarta.validation.constraints.NotNull

data class CalendarListRequest(
	@NotNull
	val year: Int,
	@NotNull
	val month: Int
) {
	fun toParam(user: User): CalendarListParam {
		return CalendarListParam(year, month, user)
	}
}
