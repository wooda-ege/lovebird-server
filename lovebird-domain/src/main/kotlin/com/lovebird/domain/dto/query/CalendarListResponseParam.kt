package com.lovebird.domain.dto.query

import com.lovebird.common.enums.Alarm
import com.lovebird.common.enums.Color
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDate
import java.time.LocalTime

data class CalendarListResponseParam @QueryProjection constructor(
	val id: Long,
	val userId: Long,
	val title: String,
	val memo: String?,
	val color: Enum<Color>,
	val alarm: Enum<Alarm>,
	val startDate: LocalDate,
	val endDate: LocalDate,
	val startTime: LocalTime?,
	val endTime: LocalTime?
)
