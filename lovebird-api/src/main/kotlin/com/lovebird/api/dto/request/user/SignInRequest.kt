package com.lovebird.api.dto.request.user

import com.lovebird.api.dto.param.user.SignInParam
import com.lovebird.common.enums.Provider

sealed class SignInRequest(
	open val provider: Provider
) {

	data class OidcUserRequest(
		override val provider: Provider,
		val idToken: String
	) : SignInRequest(provider) {
		fun toParam(): SignInParam.OidcUserParam {
			return SignInParam.OidcUserParam(
				provider = provider,
				idToken = idToken
			)
		}
	}

	data class NaverUserRequest(
		override val provider: Provider,
		val code: String,
		val state: String
	) : SignInRequest(provider) {
		fun toParam(): SignInParam.NaverUserParam {
			return SignInParam.NaverUserParam(
				provider = provider,
				code = code,
				state = state
			)
		}
	}
}
