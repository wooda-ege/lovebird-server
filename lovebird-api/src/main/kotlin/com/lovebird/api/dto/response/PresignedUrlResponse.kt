package com.lovebird.api.dto.response

data class PresignedUrlResponse(
	val presignedUrl: String,
	val filename: String
)
