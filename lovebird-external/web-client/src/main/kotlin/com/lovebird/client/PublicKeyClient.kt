package com.lovebird.client

import com.lovebird.vo.key.PublicKeyList

interface PublicKeyClient {

	fun getPublicKeys(): PublicKeyList
}
