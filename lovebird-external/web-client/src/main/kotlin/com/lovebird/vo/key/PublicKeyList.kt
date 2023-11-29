package com.lovebird.vo.key

import com.lovebird.common.exception.LbException
import com.lovebird.enums.ReturnCode

class PublicKeyList(private val keys: List<PublicKey>) {

	fun getMatchedKey(kid: String?, alg: String?): PublicKey {
		return keys.stream()
			.filter { key: PublicKey ->
				key.kid == kid && key.alg == alg
			}
			.findAny()
			.orElseThrow { LbException(ReturnCode.EXTERNAL_SERVER_ERROR) }
	}
}
