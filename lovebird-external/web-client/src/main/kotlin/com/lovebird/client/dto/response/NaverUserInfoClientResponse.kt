package com.lovebird.client.dto.response

data class NaverUserInfoClientResponse(
	val resultCode: String,
	val message: String,
	val response: NaverUserInfoResponse
)
