package com.lovebird.api.dto.response.calendar

import com.lovebird.domain.dto.query.CalendarListResponseParam

data class CalendarListResponse(
	val calendars: List<CalendarListResponseParam>,
	val totalCount: Int
) {
	companion object {
		fun of(calendars: List<CalendarListResponseParam>): CalendarListResponse {
			return CalendarListResponse(calendars, calendars.size)
		}
	}
}
