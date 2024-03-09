package com.lovebird.domain.dto.query

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDate

data class ProfileUserResponseParam @QueryProjection constructor(
	val userId: Long,
	val coupleEntryId: Long?,
	val email: String,
	val nickname: String,
	val firstDate: LocalDate?,
	val birthday: LocalDate?,
	val profileImageUrl: String?
)
