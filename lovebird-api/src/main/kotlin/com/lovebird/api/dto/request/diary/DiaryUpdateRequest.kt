package com.lovebird.api.dto.request.diary

import com.lovebird.api.dto.param.diary.DiaryUpdateParam
import java.time.LocalDate

data class DiaryUpdateRequest(
	val title: String?,
	val memoryDate: LocalDate?,
	val place: String?,
	val content: String?,
	val imageUrls: List<String>?
) {
	fun toParam(diaryId: Long): DiaryUpdateParam {
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
