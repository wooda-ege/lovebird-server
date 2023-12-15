package com.lovebird.domain.dto.query

import com.lovebird.common.enums.Alarm
import com.lovebird.common.enums.Color
import java.time.LocalDate
import java.time.LocalTime

data class CalendarListResponseParam(
	val id: Long,
	val title: String,
	val memo: String?,
	val startDate: LocalDate,
	val endDate: LocalDate?,
	val startTime: LocalTime?,
	val endTime: LocalTime?,
	val color: Enum<Color>,
	val alarm: Enum<Alarm>
)
