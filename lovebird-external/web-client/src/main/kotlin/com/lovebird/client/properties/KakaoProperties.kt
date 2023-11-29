package com.lovebird.client.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class KakaoProperties(
	@Value("\${oauth.kakao.public-key-info}")
	private val publicKeyUrl: String
) {

	fun getPublicKeyUrl(): String {
		return publicKeyUrl
	}
}
