package com.lovebird.common.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object DateUtils {
	fun toLocalDateTime(startDate: LocalDate, startTime: LocalTime?): LocalDateTime {
		return LocalDateTime.of(startDate, startTime ?: LocalTime.MIDNIGHT)
	}

	fun getPlusDaysFromNow(days: Long): LocalDate {
		return LocalDate.now().plusDays(days)
	}

	fun getPlusYearsFromNow(years: Long): LocalDate {
		return LocalDate.now().plusYears(years)
	}

	fun betweenDays(startDate: LocalDate, endDate: LocalDate): Long {
		return ChronoUnit.DAYS.between(startDate, endDate)
	}
}
