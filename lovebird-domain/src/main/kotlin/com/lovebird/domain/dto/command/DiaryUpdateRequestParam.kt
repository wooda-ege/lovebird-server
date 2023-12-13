package com.lovebird.domain.dto.command

import java.time.LocalDate

data class DiaryUpdateRequestParam(
	val title: String?,
	val memoryDate: LocalDate?,
	val place: String?,
	val content: String?
)
