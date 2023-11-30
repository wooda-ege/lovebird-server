package com.lovebird.api.dto.request.user

import com.lovebird.api.dto.param.auth.ProfileCreateParam
import com.lovebird.api.dto.param.auth.UserRegisterParam
import com.lovebird.common.enums.Gender
import com.lovebird.common.enums.Provider
import com.lovebird.domain.entity.User
import java.time.LocalDate

sealed class SingUpRequest(
	open val provider: Provider,
	open val deviceToken: String? = null,
	open val imageUrl: String,
	open val email: String,
	open val nickname: String,
	open val birthday: LocalDate?,
	open val firstDate: LocalDate?,
	open val gender: Gender
) {

	data class OidcUserRequest(
		override val provider: Provider,
		override val deviceToken: String?,
		override val imageUrl: String,
		override val email: String,
		override val nickname: String,
		override val birthday: LocalDate?,
		override val firstDate: LocalDate?,
		override val gender: Gender,
		val idToken: String
	) : SingUpRequest(
		provider = provider,
		deviceToken = deviceToken,
		imageUrl = imageUrl,
		email = email,
		nickname = nickname,
		birthday = birthday,
		firstDate = firstDate,
		gender = gender
	) {
		fun toUserRegisterParam(): UserRegisterParam.OidcUserParam {
			return UserRegisterParam.OidcUserParam(
				provider = provider,
				deviceToken = deviceToken,
				idToken = idToken
			)
		}

		fun toProfileCreateParam(user: User): ProfileCreateParam {
			return ProfileCreateParam(
				user = user,
				imageUrl = imageUrl,
				email = email,
				nickname = nickname,
				birthday = birthday,
				firstDate = firstDate,
				gender = gender
			)
		}
	}

	data class NaverUserRequest(
		override val provider: Provider,
		override val deviceToken: String?,
		override val imageUrl: String,
		override val email: String,
		override val nickname: String,
		override val birthday: LocalDate?,
		override val firstDate: LocalDate?,
		override val gender: Gender,
		val code: String,
		val state: String
	) : SingUpRequest(
		provider = provider,
		deviceToken = deviceToken,
		imageUrl = imageUrl,
		email = email,
		nickname = nickname,
		birthday = birthday,
		firstDate = firstDate,
		gender = gender
	) {
		fun toUserRegisterParam(): UserRegisterParam.NaverUserParam {
			return UserRegisterParam.NaverUserParam(
				provider = provider,
				deviceToken = deviceToken,
				code = code,
				state = state
			)
		}

		fun toProfileCreateParam(user: User): ProfileCreateParam {
			return ProfileCreateParam(
				user = user,
				imageUrl = imageUrl,
				email = email,
				nickname = nickname,
				birthday = birthday,
				firstDate = firstDate,
				gender = gender
			)
		}
	}
}
