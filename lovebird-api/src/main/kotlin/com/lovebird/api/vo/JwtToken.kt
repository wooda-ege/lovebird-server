package com.lovebird.api.vo

data class JwtToken(
	val accessToken: String,
	val refreshToken: String,
	val grantType: String
)
