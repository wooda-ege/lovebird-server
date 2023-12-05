package com.lovebird.api.dto.request.profile

import com.lovebird.common.enums.AnniversaryType
import java.time.LocalDate

data class AnniversaryResponse(
	val kind: AnniversaryType,
	val anniversaryDate: LocalDate
)
