package com.lovebird.client.properties

import com.lovebird.client.dto.request.NaverLoginClientRequest
import org.springframework.beans.factory.annotation.Value

class NaverProperties {

	@Value("\${oauth.naver.client-id}")
	lateinit var clientId: String

	@Value("\${oauth.naver.client-secret}")
	lateinit var clientSecret: String

	@Value("\${oauth.naver.grant-type}")
	lateinit var grantType: String

	@Value("\${oauth.naver.url.token}")
	lateinit var tokenUrl: String

	@Value("\${oauth.naver.url.user-info}")
	lateinit var userInfoUrl: String

	fun toNaverLoginRequest(code: String, state: String): NaverLoginClientRequest {
		return NaverLoginClientRequest(
			clientId = clientId,
			clientSecret = clientSecret,
			code = code,
			state = state,
			grantType = grantType
		)
	}
}
