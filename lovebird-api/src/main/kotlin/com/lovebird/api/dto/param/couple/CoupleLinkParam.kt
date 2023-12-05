package com.lovebird.api.dto.param.couple

import com.lovebird.domain.entity.User

data class CoupleLinkParam(
	val user: User,
	val coupleCode: String
)
