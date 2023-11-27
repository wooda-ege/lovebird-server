package com.lovebird.dto.param

data class DiaryUploadPresignedUrlParam(
	val userId: Long,
	val diaryId: Long,
	val filenames: List<String>
)
