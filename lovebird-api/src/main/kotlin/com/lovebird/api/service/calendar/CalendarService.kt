package com.lovebird.api.service.calendar

import com.lovebird.domain.repository.reader.CalendarReader
import com.lovebird.domain.repository.writer.CalendarWriter
import org.springframework.stereotype.Service

@Service
class CalendarService(
	private val calendarReader: CalendarReader,
	private val calendarWriter: CalendarWriter
)
