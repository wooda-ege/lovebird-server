package com.lovebird.api.dto.param.profile

import com.lovebird.common.enums.Gender
import com.lovebird.domain.entity.Profile
import com.lovebird.domain.entity.User
import java.time.LocalDate

data class ProfileCreateParam(
	val user: User,
	val imageUrl: String,
	val email: String,
	val nickname: String,
	val birthday: LocalDate?,
	val firstDate: LocalDate?,
	val gender: Gender
) {
	fun toEntity(): Profile {
		return Profile(
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
