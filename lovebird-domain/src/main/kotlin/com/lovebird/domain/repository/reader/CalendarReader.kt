package com.lovebird.domain.repository.reader

import com.lovebird.domain.annotation.Reader
import com.lovebird.domain.repository.jpa.CalendarJpaRepository

@Reader
class CalendarReader(
	private val calendarJpaRepository: CalendarJpaRepository
)
