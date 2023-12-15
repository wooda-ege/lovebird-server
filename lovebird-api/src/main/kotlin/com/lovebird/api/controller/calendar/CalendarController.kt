package com.lovebird.api.controller.calendar

import com.lovebird.api.dto.response.calendar.CalendarDetailResponse
import com.lovebird.api.service.calendar.CalendarService
import com.lovebird.common.response.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/calendars")
class CalendarController(
	private val calendarService: CalendarService
) {

	@GetMapping("/{id}")
	fun getCalendarById(@PathVariable id: Long): ApiResponse<CalendarDetailResponse> {
		return ApiResponse.success(calendarService.findById(id))
	}
}
