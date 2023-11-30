package com.lovebird.common.enums

import com.lovebird.common.exception.LbException

enum class Provider {

	NAVER,
	KAKAO,
	GOOGLE,
	APPLE;

	fun of(provider: String): Provider {
		return provider.uppercase().let {
			when (it) {
				"NAVER" -> NAVER
				"KAKAO" -> KAKAO
				"GOOGLE" -> GOOGLE
				"APPLE" -> APPLE
				else -> throw LbException(ReturnCode.WRONG_PARAMETER)
			}
		}
	}
}
