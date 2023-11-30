package com.lovebird.api.dto.response.external

data class PresignedUrlResponse(
	val presignedUrl: String,
	val filename: String
)
