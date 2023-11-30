package com.lovebird.api.dto.request

data class DiaryUploadPresignedUrlRequest(
	val filenames: List<String>,
	val diaryId: Long
)
