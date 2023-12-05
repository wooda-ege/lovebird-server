package com.lovebird.domain.dto.query

import com.lovebird.common.enums.AnniversaryType
import java.time.LocalDate

data class ProfileDetailParam(
	val userId: Long,
	val partnerId: Long,
	val email: String,
	val nickname: String,
	val partnerNickname: String?,
	val firstDate: LocalDate?,
	val birthday: LocalDate?,
	val nextAnniversaryType: AnniversaryType?,
	val nextAnniversaryDate: LocalDate?,
	val profileImageUrl: String,
	val partnerImageUrl: String?
)
