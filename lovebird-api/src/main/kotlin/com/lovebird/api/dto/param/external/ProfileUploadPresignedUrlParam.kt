package com.lovebird.api.dto.param.external

data class ProfileUploadPresignedUrlParam(
	val userId: Long,
	val filename: String
)
