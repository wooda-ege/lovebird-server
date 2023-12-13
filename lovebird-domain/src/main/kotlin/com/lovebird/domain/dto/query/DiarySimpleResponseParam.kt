package com.lovebird.domain.dto.query

import java.time.LocalDate

data class DiarySimpleResponseParam(
	val diaryId: Long,
	val userId: Long,
	val title: String,
	val memoryDate: LocalDate,
	val place: String?,
	val content: String?,
	val imageUrl: String?
)
