package com.lovebird.api.dto.response.diary

import com.lovebird.domain.dto.query.DiaryResponseParam
import java.time.LocalDate

data class DiaryListResponse(
	val diaries: List<DiaryResponseParam>?,
	val totalCount: Int = diaries?.size ?: 0,
	val diaryId: Long? = diaries?.last()?.diaryId,
	val memoryDate: LocalDate? = diaries?.last()?.memoryDate
)
