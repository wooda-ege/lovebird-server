package com.lovebird.domain.dto.query

import java.time.LocalDate

data class DiarySimpleRequestParam(
	val userId: Long,
	val partnerId: Long?,
	val memoryDate: LocalDate
)
