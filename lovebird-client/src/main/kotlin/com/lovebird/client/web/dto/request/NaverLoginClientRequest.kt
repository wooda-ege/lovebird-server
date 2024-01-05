package com.lovebird.client.web.dto.request

import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

data class NaverLoginClientRequest(
	val clientId: String,
	val clientSecret: String,
	val code: String,
	val grantType: String,
	val state: String
) {
	fun toParamMap(): MultiValueMap<String, String> {
		val body: MultiValueMap<String, String> = LinkedMultiValueMap()

		body.add("grant_type", "authorization_code")
		body.add("client_id", clientId)
		body.add("client_secret", clientSecret)
		body.add("code", code)
		body.add("state", state)

		return body
	}
}
