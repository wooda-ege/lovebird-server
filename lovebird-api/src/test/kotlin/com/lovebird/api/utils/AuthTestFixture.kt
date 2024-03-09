package com.lovebird.api.utils

import com.lovebird.api.dto.response.user.RecreateTokenResponse
import com.lovebird.api.vo.JwtToken

object AuthTestFixture {

	fun getRecreateTokenResponse(): RecreateTokenResponse {
		return RecreateTokenResponse("access-token", "refresh-token")
	}

	fun getJwtToken(): JwtToken {
		return JwtToken("access token", "refresh token", "bearer")
	}
}
