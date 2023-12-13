package com.lovebird.api.dto.request.diary

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
}
