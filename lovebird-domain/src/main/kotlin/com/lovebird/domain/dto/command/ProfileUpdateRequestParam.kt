package com.lovebird.domain.dto.command

import com.lovebird.common.enums.Gender
import java.time.LocalDate

data class ProfileUpdateRequestParam(
	val imageUrl: String?,
	val email: String?,
	val nickname: String?,
	val birthday: LocalDate?,
	val firstDate: LocalDate?,
	val gender: Gender?
)
