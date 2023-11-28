package com.lovebird.client.dto.request

data class NaverLoginClientRequest(
	val clientId: String,
	val clientSecret: String,
	val code: String,
	val grantType: String,
	val state: String
)
