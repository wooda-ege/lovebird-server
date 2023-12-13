package com.lovebird.api.dto.param.diary

import com.lovebird.domain.dto.command.DiaryUpdateRequestParam
import java.time.LocalDate

data class DiaryUpdateParam(
	val diaryId: Long,
	val title: String?,
	val memoryDate: LocalDate?,
	val place: String?,
	val content: String?,
	val imageUrls: List<String>?
) {
	fun toDomainParam(): DiaryUpdateRequestParam {
		return DiaryUpdateRequestParam(
			title = title,
			memoryDate = memoryDate,
			place = place,
			content = content
		)
	}
}
