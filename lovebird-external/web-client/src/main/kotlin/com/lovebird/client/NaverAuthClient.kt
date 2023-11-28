package com.lovebird.client

import com.lovebird.client.dto.request.NaverLoginClientRequest
import com.lovebird.client.dto.request.NaverUserInfoClientRequest
import com.lovebird.client.dto.response.NaverLoginClientResponse
import com.lovebird.client.dto.response.NaverUserInfoClientResponse
import com.lovebird.client.properties.NaverProperties
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class NaverAuthClient(
	private val webClient: WebClient,
	private val properties: NaverProperties
) {

	fun getAccessToken(request: NaverLoginClientRequest): NaverLoginClientResponse {
		return webClient.mutate()
			.baseUrl(properties.tokenUrl)
			.build()
			.post()
			.bodyValue(request)
			.retrieve()
			.bodyToMono(NaverLoginClientResponse::class.java)
			.block()!!
	}

	fun getUserInfo(request: NaverUserInfoClientRequest): NaverUserInfoClientResponse {
		return webClient.mutate()
			.baseUrl(properties.userInfoUrl)
			.build()
			.get()
			.header(AUTHORIZATION, request.accessToken)
			.retrieve()
			.bodyToMono(NaverUserInfoClientResponse::class.java)
			.block()!!
	}
}
