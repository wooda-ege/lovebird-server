package com.lovebird.api.dto.response.user

import com.lovebird.api.vo.JwtToken

data class SignUpResponse(
	val accessToken: String,
	val refreshToken: String
) {
	companion object {
		fun from(jwtToken: JwtToken): SignUpResponse {
			return SignUpResponse(
				accessToken = jwtToken.accessToken,
				refreshToken = jwtToken.refreshToken
			)
		}
	}
}
