package com.lovebird.api.dto.request.user

import com.lovebird.api.dto.param.profile.ProfileCreateParam
import com.lovebird.api.dto.param.user.SignUpParam
import com.lovebird.common.enums.Gender
import com.lovebird.common.enums.Provider
import com.lovebird.domain.entity.User
import java.time.LocalDate

sealed class SignUpRequest(
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
	) : SignUpRequest(
		provider = provider,
		deviceToken = deviceToken,
		imageUrl = imageUrl,
		email = email,
		nickname = nickname,
		birthday = birthday,
		firstDate = firstDate,
		gender = gender
	) {
		fun toUserRegisterParam(): SignUpParam.OidcUserParam {
			return SignUpParam.OidcUserParam(
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
	) : SignUpRequest(
		provider = provider,
		deviceToken = deviceToken,
		imageUrl = imageUrl,
		email = email,
		nickname = nickname,
		birthday = birthday,
		firstDate = firstDate,
		gender = gender
	) {
		fun toUserRegisterParam(): SignUpParam.NaverUserParam {
			return SignUpParam.NaverUserParam(
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
