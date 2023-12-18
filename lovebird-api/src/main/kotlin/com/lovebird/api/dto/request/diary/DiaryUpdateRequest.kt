package com.lovebird.api.dto.request.diary

import com.lovebird.api.dto.param.diary.DiaryUpdateParam
import java.time.LocalDate

data class DiaryUpdateRequest(
	val diaryId: Long,
	val title: String?,
	val memoryDate: LocalDate?,
	val place: String?,
	val content: String?,
	val imageUrls: List<String>?
) {
	fun toParam(): DiaryUpdateParam {
		return DiaryUpdateParam(
			diaryId = diaryId,
			title = title,
			memoryDate = memoryDate,
			place = place,
			content = content,
			imageUrls = imageUrls
		)
	}
}
