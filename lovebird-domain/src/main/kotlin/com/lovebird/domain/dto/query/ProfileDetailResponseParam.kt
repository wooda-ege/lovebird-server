package com.lovebird.domain.dto.query

import java.time.LocalDate

data class ProfileDetailResponseParam(
	val userId: Long,
	val partnerId: Long,
	val email: String,
	val nickname: String,
	val partnerNickname: String?,
	val firstDate: LocalDate?,
	val birthday: LocalDate?,
	val partnerBirthday: LocalDate?,
	val profileImageUrl: String,
	val partnerImageUrl: String?
)
