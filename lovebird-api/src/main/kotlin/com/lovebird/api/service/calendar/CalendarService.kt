package com.lovebird.api.service.calendar

import com.lovebird.api.dto.param.calendar.CalendarListParam
import com.lovebird.api.dto.response.calendar.CalendarDetailResponse
import com.lovebird.api.dto.response.calendar.CalendarListResponse
import com.lovebird.domain.dto.query.CalendarListResponseParam
import com.lovebird.domain.entity.Calendar
import com.lovebird.domain.entity.CoupleEntry
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.reader.CalendarReader
import com.lovebird.domain.repository.reader.CoupleEntryReader
import com.lovebird.domain.repository.writer.CalendarWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CalendarService(
	private val calendarReader: CalendarReader,
	private val coupleEntryReader: CoupleEntryReader,
	private val calendarWriter: CalendarWriter
) {

	@Transactional(readOnly = true)
	fun findById(id: Long): CalendarDetailResponse {
		val calendar: Calendar = calendarReader.findEntityById(id)

		return CalendarDetailResponse.of(calendar)
	}

	@Transactional(readOnly = true)
	fun findCalendarsByMonthAndUser(param: CalendarListParam): CalendarListResponse {
		val coupleEntry: CoupleEntry? = coupleEntryReader.findByUser(param.user)

		return if (coupleEntry != null) {
			val partner: User = coupleEntry.partner
			val calendars: List<CalendarListResponseParam> = calendarReader
				.findCalendarsByDate(param.toRequestParam(partner))

			CalendarListResponse.of(calendars)
		} else {
			CalendarListResponse.of(calendarReader.findCalendarsByDate(param.toRequestParam()))
		}
	}
}
