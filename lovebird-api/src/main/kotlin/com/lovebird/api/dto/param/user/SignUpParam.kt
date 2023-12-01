package com.lovebird.api.dto.param.user

import com.lovebird.common.enums.Provider
import com.lovebird.domain.entity.User

sealed class SignUpParam(
	open val provider: Provider,
	open val deviceToken: String? = null
) {

	data class OidcUserParam(
		override val provider: Provider,
		override val deviceToken: String? = null,
		val idToken: String
	) : SignUpParam(provider) {

		fun toUserEntity(providerId: String): User {
			return User(
				provider = provider,
				providerId = providerId,
				deviceToken = deviceToken
			)
		}
	}

	data class NaverUserParam(
		override val provider: Provider,
		override val deviceToken: String? = null,
		val code: String,
		val state: String
	) : SignUpParam(provider) {

		fun toUserEntity(providerId: String): User {
			return User(
				provider = provider,
				providerId = providerId,
				deviceToken = deviceToken
			)
		}
	}
}
