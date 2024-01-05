package com.lovebird.client.web

import com.lovebird.client.vo.key.OidcPublicKeyList

interface PublicKeyClient {

	fun getPublicKeys(): OidcPublicKeyList
}
