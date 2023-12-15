package com.lovebird.domain.repository.reader

import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.lovebird.domain.annotation.Reader
import com.lovebird.domain.dto.query.CalendarListResponseParam
import com.lovebird.domain.entity.Calendar
import com.lovebird.domain.repository.jpa.CalendarJpaRepository
import com.lovebird.domain.repository.query.CalendarQueryRepository

@Reader
class CalendarReader(
	private val calendarJpaRepository: CalendarJpaRepository,
	private val calendarQueryRepository: CalendarQueryRepository
) {

	fun findEntityById(id: Long): Calendar {
		return calendarJpaRepository.findById(id).orElseThrow() { throw LbException(ReturnCode.WRONG_PARAMETER) }
	}

	fun findCalendarsByDateAndUserIdAndPartnerId(
		year: Int,
		month: Int,
		userId: Long,
		partnerId: Long
	): List<CalendarListResponseParam> {
		return calendarQueryRepository.findCalendarsByDateAndUserIdAndPartnerId(year, month, userId, partnerId)
	}
}
