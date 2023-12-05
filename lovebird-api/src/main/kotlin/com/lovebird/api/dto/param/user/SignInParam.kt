package com.lovebird.api.dto.param.user

import com.lovebird.common.enums.Provider

sealed class SignInParam(
	open val provider: Provider
) {
	data class OidcUserParam(
		override val provider: Provider,
		val idToken: String
	) : SignInParam(provider)

	data class NaverUserParam(
		override val provider: Provider,
		val code: String,
		val state: String
	) : SignInParam(provider)
}
