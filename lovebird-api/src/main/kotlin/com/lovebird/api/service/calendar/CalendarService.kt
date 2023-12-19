package com.lovebird.api.service.calendar

import com.lovebird.api.dto.param.calendar.CalendarListParam
import com.lovebird.api.dto.param.calendar.CalendarUpdateParam
import com.lovebird.api.dto.request.calendar.CalendarCreateRequest
import com.lovebird.api.dto.request.calendar.CalendarUpdateRequest
import com.lovebird.api.dto.response.calendar.CalendarDetailResponse
import com.lovebird.api.dto.response.calendar.CalendarListResponse
import com.lovebird.common.util.DateUtils
import com.lovebird.domain.dto.query.CalendarEventRequestParam
import com.lovebird.domain.dto.query.CalendarListResponseParam
import com.lovebird.domain.entity.Calendar
import com.lovebird.domain.entity.CalendarEvent
import com.lovebird.domain.entity.CoupleEntry
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.reader.CalendarEventReader
import com.lovebird.domain.repository.reader.CalendarReader
import com.lovebird.domain.repository.reader.CoupleEntryReader
import com.lovebird.domain.repository.writer.CalendarEventWriter
import com.lovebird.domain.repository.writer.CalendarWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CalendarService(
	private val calendarReader: CalendarReader,
	private val coupleEntryReader: CoupleEntryReader,
	private val calendarWriter: CalendarWriter,
	private val calendarEventWriter: CalendarEventWriter,
	private val calendarEventReader: CalendarEventReader
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

	@Transactional
	fun save(request: CalendarCreateRequest, user: User) {
		val calendar: Calendar = calendarWriter.save(request.toEntity(user))

		val coupleEntry: CoupleEntry? = coupleEntryReader.findByUser(user)
		val eventAt: LocalDateTime = DateUtils.toLocalDateTime(request.startDate, request.startTime)

		calendarEventWriter.save(CalendarEventRequestParam(calendar, calendar.user, eventAt))

		if (coupleEntry != null) {
			calendarEventWriter.save(CalendarEventRequestParam(calendar, coupleEntry.partner, eventAt))
		}
	}

	@Transactional
	fun update(calendarUpdateParam: CalendarUpdateParam) {
		val calendarId: Long = calendarUpdateParam.calendarId
		val user: User = calendarUpdateParam.user
		val request: CalendarUpdateRequest = calendarUpdateParam.request
		val calendar: Calendar = calendarReader.findEntityById(calendarId)

		calendar.updateCalendar(request.toEntity(user))

		val newEventAt: LocalDateTime = DateUtils.toLocalDateTime(request.startDate, request.startTime)
		val calendarEvents: List<CalendarEvent> = calendarEventReader.findCalendarEventsByCalendar(calendar)

		calendarEvents.forEach { calendarEvent ->
			calendarEvent.updateCalendarEvent(newEventAt, request.alarm)
		}
	}

	@Transactional
	fun delete(calendarId: Long, user: User) {
		val calendar: Calendar = calendarReader.findEntityByIdAndUser(calendarId, user)
		val calendarEvents: List<CalendarEvent> = calendarEventReader.findCalendarEventsByCalendar(calendar)

		calendarWriter.delete(calendar)
		calendarEventWriter.deleteAll(calendarEvents)
	}
}
