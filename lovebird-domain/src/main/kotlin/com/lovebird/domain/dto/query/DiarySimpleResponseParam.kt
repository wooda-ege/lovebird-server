package com.lovebird.domain.dto.query

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDate

data class DiarySimpleResponseParam @QueryProjection constructor(
	val diaryId: Long,
	val userId: Long,
	var title: String,
	val memoryDate: LocalDate,
	var place: String?,
	var content: String?,
	val imageUrl: String?
)
