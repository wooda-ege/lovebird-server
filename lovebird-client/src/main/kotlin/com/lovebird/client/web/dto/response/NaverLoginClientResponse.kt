package com.lovebird.client.web.dto.response

data class NaverLoginClientResponse(
	val accessToken: String,
	val refreshToken: String,
	val tokenType: String,
	val expiresIn: Int,
	val error: String,
	val errorDescription: String
)
