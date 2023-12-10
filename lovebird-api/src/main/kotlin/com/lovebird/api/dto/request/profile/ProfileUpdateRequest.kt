package com.lovebird.api.dto.request.profile

import com.lovebird.common.enums.Gender
import com.lovebird.domain.dto.command.ProfileUpdateRequestParam
import java.time.LocalDate

data class ProfileUpdateRequest(
	val imageUrl: String?,
	val email: String?,
	val nickname: String?,
	val birthday: LocalDate?,
	val firstDate: LocalDate?,
	val gender: Gender?
) {
	fun toParam(): ProfileUpdateRequestParam {
		return ProfileUpdateRequestParam(
			imageUrl = imageUrl,
			email = email,
			nickname = nickname,
			birthday = birthday,
			firstDate = firstDate,
			gender = gender
		)
	}
}
