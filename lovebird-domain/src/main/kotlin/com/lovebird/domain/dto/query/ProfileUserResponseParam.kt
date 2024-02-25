package com.lovebird.domain.dto.query

import java.time.LocalDate

data class ProfileUserResponseParam(
	val userId: Long,
	val coupleEntryId: Long?,
	val email: String,
	val nickname: String,
	val firstDate: LocalDate?,
	val birthday: LocalDate?,
	val profileImageUrl: String
)
