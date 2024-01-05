package com.lovebird.client.web.dto.response

data class NaverUserInfoClientResponse(
	val resultCode: String,
	val message: String,
	val response: Map<String, String>
)
