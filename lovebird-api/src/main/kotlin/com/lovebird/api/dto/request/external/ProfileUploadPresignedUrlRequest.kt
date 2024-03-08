package com.lovebird.api.dto.request.external

import com.lovebird.api.dto.param.external.ProfileUploadPresignedUrlParam

data class ProfileUploadPresignedUrlRequest(
	val providerId: String,
	val filename: String
) {
	fun toParam(): ProfileUploadPresignedUrlParam {
		return ProfileUploadPresignedUrlParam(providerId, filename)
	}
}
