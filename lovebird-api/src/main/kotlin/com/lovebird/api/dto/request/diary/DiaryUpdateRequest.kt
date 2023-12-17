package com.lovebird.api.dto.request.diary

import com.lovebird.api.dto.param.diary.DiaryUpdateParam
import com.lovebird.api.provider.AesEncryptProvider.encryptString
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
			title = title?.let { encryptString(it) },
			memoryDate = memoryDate,
			place = place?.let { encryptString(it) },
			content = content?.let { encryptString(it) },
			imageUrls = imageUrls
		)
	}
}
