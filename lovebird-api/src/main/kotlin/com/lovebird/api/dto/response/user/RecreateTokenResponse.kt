package com.lovebird.api.dto.response.user

import com.lovebird.api.vo.JwtToken

data class RecreateTokenResponse(
	val accessToken: String,
	val refreshToken: String
) {
	companion object {

		fun of(jwtToken: JwtToken): RecreateTokenResponse {
			return RecreateTokenResponse(jwtToken.accessToken, jwtToken.refreshToken)
		}
	}
}
