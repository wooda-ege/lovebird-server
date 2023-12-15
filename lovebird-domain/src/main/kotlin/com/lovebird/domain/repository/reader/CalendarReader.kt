package com.lovebird.domain.repository.reader

import com.lovebird.domain.annotation.Reader
import com.lovebird.domain.entity.Calendar
import com.lovebird.domain.repository.jpa.CalendarJpaRepository
import jakarta.persistence.EntityNotFoundException

@Reader
class CalendarReader(
	private val calendarJpaRepository: CalendarJpaRepository
) {

	fun findEntityById(id: Long): Calendar {
		return calendarJpaRepository.findById(id).orElseThrow() { throw EntityNotFoundException() }
	}
}
