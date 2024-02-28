package com.lovebird.api.dto.response.diary

import com.lovebird.domain.dto.query.DiaryResponseParam

data class DiarySimpleResponse(
	val diaries: List<DiaryResponseParam>,
	val totalCount: Int = diaries.size
) {
	companion object {
		fun of(diaries: List<DiaryResponseParam>): DiarySimpleResponse {
			return DiarySimpleResponse(diaries)
		}
	}
}
