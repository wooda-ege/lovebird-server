package com.lovebird.api.util

import com.lovebird.api.dto.request.profile.AnniversaryResponse
import com.lovebird.common.enums.AnniversaryType
import com.lovebird.common.util.DateUtils.betweenDays
import com.lovebird.common.util.DateUtils.getPlusDaysFromNow
import com.lovebird.common.util.DateUtils.getPlusYearsFromNow
import java.time.LocalDate

object ProfileUtils {

	fun getNextAnniversary(firstDate: LocalDate): AnniversaryResponse {
		val days: Long = betweenDays(firstDate, LocalDate.now())

		return if (days < 300) getAnniversaryOfDay(days, firstDate) else getAnniversaryOfYear(days, firstDate)
	}

	private fun getAnniversaryOfDay(days: Long, firstDate: LocalDate): AnniversaryResponse {
		return if (days < 100) {
			AnniversaryResponse.of(AnniversaryType.DAY, 100, getPlusDaysFromNow(100, firstDate))
		} else if (days < 200) {
			AnniversaryResponse.of(AnniversaryType.DAY, 200, getPlusDaysFromNow(200, firstDate))
		} else {
			AnniversaryResponse.of(AnniversaryType.DAY, 300, getPlusDaysFromNow(300, firstDate))
		}
	}

	private fun getAnniversaryOfYear(days: Long, firstDate: LocalDate): AnniversaryResponse {
		val year: Long = days / 365 + 1
		return AnniversaryResponse.of(AnniversaryType.YEAR, year.toInt(), getPlusYearsFromNow(year, firstDate))
	}
}
