package com.lovebird.webClient.client

import com.lovebird.webClient.client.properties.KakaoProperties
import com.lovebird.webClient.vo.key.OidcPublicKeyList
import org.springframework.web.reactive.function.client.WebClient

class KakaoAuthClient(
	private val webClient: WebClient,
	private val properties: KakaoProperties
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
