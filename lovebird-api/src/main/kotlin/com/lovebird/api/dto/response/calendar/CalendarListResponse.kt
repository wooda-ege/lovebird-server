package com.lovebird.api.dto.response.calendar

import com.lovebird.domain.dto.query.CalendarListResponseParam

data class CalendarListResponse(
	val calendars: List<CalendarListResponseParam>
) {
	companion object {
		fun of(calendars: List<CalendarListResponseParam>): CalendarListResponse {
			return CalendarListResponse(calendars)
		}
	}
}
