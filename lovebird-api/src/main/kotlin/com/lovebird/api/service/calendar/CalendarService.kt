package com.lovebird.api.service.calendar

import com.lovebird.api.dto.response.calendar.CalendarDetailResponse
import com.lovebird.domain.entity.Calendar
import com.lovebird.domain.repository.reader.CalendarReader
import com.lovebird.domain.repository.writer.CalendarWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CalendarService(
	private val calendarReader: CalendarReader,
	private val calendarWriter: CalendarWriter
) {

	@Transactional(readOnly = true)
	fun findById(id: Long): CalendarDetailResponse {
		val calendar: Calendar = calendarReader.findEntityById(id)

		return CalendarDetailResponse.of(calendar)
	}
}
