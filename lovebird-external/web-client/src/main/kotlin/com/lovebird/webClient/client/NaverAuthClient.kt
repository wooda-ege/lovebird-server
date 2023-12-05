package com.lovebird.webClient.client

import com.lovebird.webClient.client.dto.request.NaverUserInfoClientRequest
import com.lovebird.webClient.client.dto.response.NaverLoginClientResponse
import com.lovebird.webClient.client.dto.response.NaverUserInfoClientResponse
import com.lovebird.webClient.client.properties.NaverProperties
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class NaverAuthClient(
	private val webClient: WebClient,
	private val properties: NaverProperties
) {

	fun getAccessToken(code: String, state: String): NaverLoginClientResponse {
		val request = properties.toNaverLoginRequest(code, state)

		return webClient.mutate()
			.baseUrl(properties.getTokenUrl())
			.build()
			.post()
			.bodyValue(request)
			.retrieve()
			.bodyToMono(NaverLoginClientResponse::class.java)
			.block()!!
	}

	fun getUserInfo(request: NaverUserInfoClientRequest): NaverUserInfoClientResponse {
		return webClient.mutate()
			.baseUrl(properties.getUserInfoUrl())
			.build()
			.get()
			.header(AUTHORIZATION, request.accessToken)
			.retrieve()
			.bodyToMono(NaverUserInfoClientResponse::class.java)
			.block()!!
	}
}
