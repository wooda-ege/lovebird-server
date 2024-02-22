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

		return if (days < 300) getAnniversaryOfDay(days) else getAnniversaryOfYear(days)
	}

	private fun getAnniversaryOfDay(days: Long): AnniversaryResponse {
		return if (days < 100) {
			AnniversaryResponse.of(AnniversaryType.DAY, days.toInt(), getPlusDaysFromNow(100))
		} else if (days < 200) {
			AnniversaryResponse.of(AnniversaryType.DAY, days.toInt(), getPlusDaysFromNow(200))
		} else {
			AnniversaryResponse.of(AnniversaryType.DAY, days.toInt(), getPlusDaysFromNow(300))
		}
	}

	private fun getAnniversaryOfYear(days: Long): AnniversaryResponse {
		val year: Long = days / 365 + 1
		return AnniversaryResponse.of(AnniversaryType.YEAR, year.toInt(), getPlusYearsFromNow(year))
	}
}
