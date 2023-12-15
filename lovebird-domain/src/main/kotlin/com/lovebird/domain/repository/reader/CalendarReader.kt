package com.lovebird.domain.repository.reader

import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.lovebird.domain.annotation.Reader
import com.lovebird.domain.entity.Calendar
import com.lovebird.domain.repository.jpa.CalendarJpaRepository

@Reader
class CalendarReader(
	private val calendarJpaRepository: CalendarJpaRepository
) {

	fun findEntityById(id: Long): Calendar {
		return calendarJpaRepository.findById(id).orElseThrow() { throw LbException(ReturnCode.WRONG_PARAMETER) }
	}
}
