package com.lovebird.common.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

object DateUtils {
	fun toLocalDateTime(startDate: LocalDate, startTime: LocalTime?): LocalDateTime {
		return LocalDateTime.of(startDate, startTime ?: LocalTime.MIDNIGHT)
	}
}
