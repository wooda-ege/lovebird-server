package com.lovebird.api.controller.calendar

import com.lovebird.api.service.calendar.CalendarService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/calendar")
class CalendarController(
	private val calendarService: CalendarService
)
