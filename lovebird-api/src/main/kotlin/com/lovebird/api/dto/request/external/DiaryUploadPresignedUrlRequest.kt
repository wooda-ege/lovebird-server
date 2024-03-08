package com.lovebird.api.dto.request.external

import com.lovebird.api.dto.param.external.DiaryUploadPresignedUrlParam

data class DiaryUploadPresignedUrlRequest(
	val filenames: List<String>,
) {
	fun toParam(userId: Long): DiaryUploadPresignedUrlParam {
		return DiaryUploadPresignedUrlParam(userId, filenames)
	}
}
