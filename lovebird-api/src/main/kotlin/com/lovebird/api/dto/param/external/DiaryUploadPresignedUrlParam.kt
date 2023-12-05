package com.lovebird.api.dto.param.external

data class DiaryUploadPresignedUrlParam(
	val userId: Long,
	val diaryId: Long,
	val filenames: List<String>
)
