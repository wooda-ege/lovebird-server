package com.lovebird.domain.dto.query

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDate

data class ProfilePartnerResponseParam @QueryProjection constructor(
	val partnerId: Long,
	val partnerNickname: String?,
	val partnerBirthday: LocalDate?,
	val partnerImageUrl: String?
)
