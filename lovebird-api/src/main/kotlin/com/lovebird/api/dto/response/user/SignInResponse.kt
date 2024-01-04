package com.lovebird.api.dto.response.user

import com.lovebird.api.vo.JwtToken

data class SignInResponse(
	val accessToken: String,
	val refreshToken: String,
	val linkedFlag: Boolean
) {
	companion object {
		fun of(jwtToken: JwtToken, linkedFlag: Boolean): SignInResponse {
			return SignInResponse(
				accessToken = jwtToken.accessToken,
				refreshToken = jwtToken.refreshToken,
				linkedFlag = linkedFlag
			)
		}
	}
}
