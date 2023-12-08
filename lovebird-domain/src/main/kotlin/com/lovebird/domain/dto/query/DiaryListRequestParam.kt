package com.lovebird.domain.dto.query

import java.time.LocalDate

data class DiaryListRequestParam(
	val userId: Long,
	val partnerId: Long,
	val memoryDate: LocalDate,
	val pageSize: Long
)
