package com.lovebird.webClient.client

import com.lovebird.webClient.vo.key.OidcPublicKeyList

interface PublicKeyClient {

	fun getPublicKeys(): OidcPublicKeyList
}
