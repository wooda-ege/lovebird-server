package com.lovebird.domain.dto.query

import java.time.LocalDate

data class ProfileDetailResponseParam(
	val userId: Long,
	val partnerId: Long?,
	val email: String,
	val nickname: String,
	val partnerNickname: String?,
	val firstDate: LocalDate?,
	val birthday: LocalDate?,
	val partnerBirthday: LocalDate?,
	val profileImageUrl: String,
	val partnerImageUrl: String?
) {
	companion object {
		fun of(user: ProfileUserResponseParam, partner: ProfilePartnerResponseParam?): ProfileDetailResponseParam {
			return ProfileDetailResponseParam(
				userId = user.userId,
				partnerId = partner?.partnerId,
				email = user.email,
				nickname = user.nickname,
				partnerNickname = partner?.partnerNickname,
				firstDate = user.firstDate,
				birthday = user.birthday,
				partnerBirthday = partner?.partnerBirthday,
				profileImageUrl = user.profileImageUrl,
				partnerImageUrl = partner?.partnerImageUrl
			)
		}
	}
}
