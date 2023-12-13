package com.lovebird.api.dto.response.diary

import com.lovebird.domain.dto.query.DiarySimpleResponseParam

data class DiarySimpleListResponse(
	val diaries: List<DiarySimpleResponseParam>,
	val totalCount: Int = diaries.size
)
