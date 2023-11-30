package com.lovebird.client

import com.lovebird.vo.key.OidcPublicKeyList

interface PublicKeyClient {

	fun getPublicKeys(): OidcPublicKeyList
}
