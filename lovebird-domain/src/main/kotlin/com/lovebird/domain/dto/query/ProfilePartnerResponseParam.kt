package com.lovebird.domain.dto.query

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDate

data class ProfilePartnerResponseParam @QueryProjection constructor(
	val partnerId: Long,
	val partnerEmail: String,
	val partnerNickname: String?,
	val firstDate: LocalDate,
	val partnerBirthday: LocalDate?,
	val partnerImageUrl: String?
)
