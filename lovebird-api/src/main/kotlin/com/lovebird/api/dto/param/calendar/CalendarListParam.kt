package com.lovebird.api.dto.param.calendar

import com.lovebird.domain.dto.query.CalenderListRequestParam
import com.lovebird.domain.entity.User

data class CalendarListParam(
	val year: Int,
	val month: Int,
	val user: User
) {
	fun toRequestParam(partner: User): CalenderListRequestParam {
		return CalenderListRequestParam(year, month, user.id!!, partner.id)
	}

	fun toRequestParam(): CalenderListRequestParam {
		return CalenderListRequestParam(year, month, user.id!!, null)
	}
}
