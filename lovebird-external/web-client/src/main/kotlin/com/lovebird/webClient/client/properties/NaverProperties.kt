package com.lovebird.webClient.client.properties

import com.lovebird.webClient.client.dto.request.NaverLoginClientRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class NaverProperties(
	@Value("\${oauth.naver.client-id}")
	private val clientId: String,
	@Value("\${oauth.naver.grant-type}")
	private val grantType: String,
	@Value("\${oauth.naver.client-secret}")
	private val clientSecret: String,
	@Value("\${oauth.naver.url.token}")
	private val tokenUrl: String,
	@Value("\${oauth.naver.url.user-info}")
	private val userInfoUrl: String
) {

	fun getTokenUrl(): String {
		return tokenUrl
	}

	fun getUserInfoUrl(): String {
		return userInfoUrl
	}

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
