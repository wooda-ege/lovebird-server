package com.lovebird.api.dto.request.diary

import com.lovebird.common.enums.DiarySearchType
import com.lovebird.domain.dto.query.DiaryListRequestParam
import com.lovebird.domain.dto.query.DiarySimpleRequestParam
import java.time.LocalDate

sealed class DiaryListRequest(
	open val memoryDate: LocalDate
) {
	data class SearchByMemoryDateRequest(
		override val memoryDate: LocalDate
	) : DiaryListRequest(memoryDate) {
		fun toParam(userId: Long, partnerId: Long?): DiarySimpleRequestParam {
			return DiarySimpleRequestParam(
				userId = userId,
				partnerId = partnerId,
				memoryDate = memoryDate
			)
		}
	}

	data class SearchByCursorRequest(
		override val memoryDate: LocalDate,
		val searchType: DiarySearchType,
		val diaryId: Long,
		val pageSize: Long
	) : DiaryListRequest(memoryDate) {
		fun toParam(userId: Long, partnerId: Long?): DiaryListRequestParam {
			return DiaryListRequestParam(
				userId = userId,
				partnerId = partnerId,
				diaryId = diaryId,
				memoryDate = memoryDate,
				pageSize = pageSize
			)
		}
	}
}
