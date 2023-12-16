package com.lovebird.api.dto.param.calendar

import com.lovebird.domain.entity.User

data class CalendarListParam(
	val year: Int,
	val month: Int,
	val user: User
) {
}
