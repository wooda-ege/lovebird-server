package com.lovebird.api.dto.request.external

import com.lovebird.api.dto.param.external.ProfileUploadPresignedUrlParam

data class ProfileUploadPresignedUrlRequest(
	val filename: String
) {
	fun toParam(userId: Long): ProfileUploadPresignedUrlParam {
		return ProfileUploadPresignedUrlParam(userId, filename)
	}
}
