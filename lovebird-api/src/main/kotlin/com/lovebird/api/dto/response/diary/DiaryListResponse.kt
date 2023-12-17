package com.lovebird.api.dto.response.diary

import com.lovebird.api.provider.AesEncryptProvider
import com.lovebird.domain.dto.query.DiaryResponseParam
import java.time.LocalDate

data class DiaryListResponse(
	val diaries: List<DiaryResponseParam>?,
	val totalCount: Int = diaries?.size ?: 0,
	val diaryId: Long? = diaries?.last()?.diaryId,
	val memoryDate: LocalDate? = diaries?.last()?.memoryDate
) {
	companion object {
		fun of(diaries: List<DiaryResponseParam>?): DiaryListResponse {
			diaries?.forEach {
				it.title = AesEncryptProvider.decryptString(it.title)
				it.place = it.place?.let { place -> AesEncryptProvider.decryptString(place) }
				it.content = it.content?.let { content -> AesEncryptProvider.decryptString(content) }
			}
			return DiaryListResponse(diaries)
		}
	}
}
