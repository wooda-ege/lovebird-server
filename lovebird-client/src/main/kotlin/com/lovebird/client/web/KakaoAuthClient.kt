package com.lovebird.client.web

import com.lovebird.client.vo.key.OidcPublicKeyList
import com.lovebird.client.web.properties.KakaoProperties
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
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
