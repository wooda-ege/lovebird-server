package com.lovebird.api.dto.request.external

import com.lovebird.api.dto.param.external.DiaryUploadPresignedUrlParam

data class DiaryUploadPresignedUrlRequest(
	val filenames: List<String>,
	val diaryId: Long
) {
	fun toParam(userId: Long): DiaryUploadPresignedUrlParam {
		return DiaryUploadPresignedUrlParam(userId, diaryId, filenames)
	}
}
