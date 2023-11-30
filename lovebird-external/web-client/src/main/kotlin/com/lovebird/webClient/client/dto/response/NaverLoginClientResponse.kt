package com.lovebird.webClient.client.dto.response

data class NaverLoginClientResponse(
	val accessToken: String,
	val refreshToken: String,
	val tokenType: String,
	val expiresIn: Int,
	val error: String,
	val errorDescription: String
)
