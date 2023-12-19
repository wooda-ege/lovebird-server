package com.lovebird.domain.dto.query

import java.time.LocalDate

data class DiaryResponseParam(
	val diaryId: Long,
	val userId: Long,
	var title: String,
	val memoryDate: LocalDate,
	var place: String?,
	var content: String?,
	val imageUrls: List<String>
)
