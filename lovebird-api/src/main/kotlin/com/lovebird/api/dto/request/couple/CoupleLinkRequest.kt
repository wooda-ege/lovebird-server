package com.lovebird.api.dto.request.couple

import com.lovebird.api.dto.param.couple.CoupleLinkParam
import com.lovebird.domain.entity.User

data class CoupleLinkRequest(
	val coupleCode: String
) {
	fun toParam(user: User): CoupleLinkParam {
		return CoupleLinkParam(user, coupleCode)
	}
}
