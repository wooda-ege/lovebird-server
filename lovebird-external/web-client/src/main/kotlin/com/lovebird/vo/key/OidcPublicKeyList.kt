package com.lovebird.vo.key

import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException

class OidcPublicKeyList(private val keys: List<OidcPublicKey>) {

	fun getMatchedKey(kid: String, alg: String): OidcPublicKey {
		return keys.stream()
			.filter { key: OidcPublicKey ->
				key.kid == kid && key.alg == alg
			}
			.findAny()
			.orElseThrow { LbException(ReturnCode.EXTERNAL_SERVER_ERROR) }
	}
}
