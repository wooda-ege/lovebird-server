package com.lovebird.security.vo

data class JwtToken(
	val accessToken: String,
	val refreshToken: String,
	val grantType: String
)
