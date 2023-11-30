package com.lovebird.webClient.vo.key

import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException

data class OidcPublicKeyList(
	val keys: List<OidcPublicKey>
) {

	fun getMatchedKey(kid: String?, alg: String?): OidcPublicKey {
		return keys.stream()
			.filter { it.kid == kid && it.alg == alg }
			.findAny()
			.orElseThrow { LbException(ReturnCode.EXTERNAL_SERVER_ERROR) }
	}
}
