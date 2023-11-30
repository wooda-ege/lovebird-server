package com.lovebird.client

import com.lovebird.client.properties.AppleProperties
import com.lovebird.vo.key.OidcPublicKeyList
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class AppleAuthClient(
	private val webClient: WebClient,
	private val properties: AppleProperties
) : PublicKeyClient {

	override fun getPublicKeys(): OidcPublicKeyList {
		return webClient.mutate()
			.baseUrl(properties.getPublicKeyUrl())
			.build()
			.get()
			.retrieve()
			.bodyToMono(OidcPublicKeyList::class.java)
			.block()!!
	}
}
