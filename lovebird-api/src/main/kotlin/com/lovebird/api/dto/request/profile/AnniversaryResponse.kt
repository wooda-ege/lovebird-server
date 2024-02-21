package com.lovebird.api.dto.request.profile

import com.lovebird.common.enums.AnniversaryType
import java.time.LocalDate

data class AnniversaryResponse(
	val kind: AnniversaryType,
	val seq: Int,
	val anniversaryDate: LocalDate
) {
	companion object {
		fun of(kind: AnniversaryType, seq: Int, anniversaryDate: LocalDate): AnniversaryResponse {
			return AnniversaryResponse(kind, seq, anniversaryDate)
		}
	}
}
