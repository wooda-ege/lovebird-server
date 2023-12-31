package com.lovebird.client.web

import com.lovebird.client.web.dto.request.NaverLoginClientRequest
import com.lovebird.client.web.dto.request.NaverUserInfoClientRequest
import com.lovebird.client.web.dto.response.NaverLoginClientResponse
import com.lovebird.client.web.dto.response.NaverUserInfoClientResponse
import com.lovebird.client.web.properties.NaverProperties
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

@Component
class NaverAuthClient(
	private val webClient: WebClient,
	private val properties: NaverProperties
) {

	fun getAccessToken(code: String, state: String): String {
		val request: NaverLoginClientRequest = properties.toNaverLoginRequest(code, state)

		return webClient.mutate()
			.baseUrl(properties.getTokenUrl())
			.build()
			.post()
			.body(BodyInserters.fromFormData(request.toParamMap()))
			.retrieve()
			.bodyToMono(NaverLoginClientResponse::class.java)
			.block()!!.accessToken
	}

	fun getUserInfo(request: NaverUserInfoClientRequest): NaverUserInfoClientResponse {
		return webClient.mutate()
			.baseUrl(properties.getUserInfoUrl())
			.build()
			.get()
			.header(AUTHORIZATION, "Bearer %s".format(request.accessToken))
			.retrieve()
			.bodyToMono(NaverUserInfoClientResponse::class.java)
			.block()!!
	}
}
