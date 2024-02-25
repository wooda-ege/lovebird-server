package com.lovebird.domain.dto.query

import java.time.LocalDate

data class ProfilePartnerResponseParam(
	val partnerId: Long,
	val partnerNickname: String?,
	val partnerBirthday: LocalDate?,
	val partnerImageUrl: String?
)
