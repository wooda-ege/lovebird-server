package com.lovebird.domain.dto.query

data class CalenderListRequestParam(
	val year: Int?,
	val month: Int?,
	val userId: Long,
	val partnerId: Long?
)
