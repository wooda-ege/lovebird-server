package com.lovebird.api.dto.response.user

import com.lovebird.security.vo.JwtToken

data class SignUpResponse(
	val accessToken: String,
	val refreshToken: String
) {
	companion object {
		fun of(jwtToken: JwtToken): SignUpResponse {
			return SignUpResponse(
				accessToken = jwtToken.accessToken,
				refreshToken = jwtToken.refreshToken
			)
		}
	}
}
