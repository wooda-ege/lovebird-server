package com.lovebird.client

import com.lovebird.client.properties.KakaoProperties
import com.lovebird.vo.key.PublicKeyList
import org.springframework.web.reactive.function.client.WebClient

class KakaoAuthClient(
	private val webClient: WebClient,
	private val properties: KakaoProperties
) : PublicKeyClient {

	override fun getPublicKeys(): PublicKeyList {
		return webClient.mutate()
			.baseUrl(properties.getPublicKeyUrl())
			.build()
			.get()
			.retrieve()
			.bodyToMono(PublicKeyList::class.java)
			.block()!!
	}
}
