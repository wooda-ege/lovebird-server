package com.lovebird.webClient.client.dto.response

data class NaverUserInfoResponse(
	val id: String,
	val name: String,
	val nickname: String,
	val email: String,
	val birthday: String,
	val profileImage: String,
	val age: String,
	val gender: String
)
