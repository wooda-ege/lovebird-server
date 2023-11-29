package com.lovebird.client.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AppleProperties(
	@Value("\${oauth.apple.public-key-url}")
	private val publicKeyUrl: String
) {

	fun getPublicKeyUrl(): String {
		return publicKeyUrl
	}
}
