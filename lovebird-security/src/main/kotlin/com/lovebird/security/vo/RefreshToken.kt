package com.lovebird.security.vo

data class RefreshToken(
	val refreshToken: String,
	val grantType: String
)
