package com.lovebird.api.utils

import com.lovebird.api.dto.response.user.AccessTokenResponse

object AuthTestFixture {

	fun getAccessTokenResponse(): AccessTokenResponse {
		return AccessTokenResponse("access-token")
	}
}
