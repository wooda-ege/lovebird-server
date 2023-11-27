package com.lovebird.dto.request

data class DiaryUploadPresignedUrlRequest(
	val filenames: List<String>,
	val diaryId: Long
)
