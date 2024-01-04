package com.lovebird.api.controller.calendar

import com.lovebird.api.annotation.AuthorizedUser
import com.lovebird.api.dto.request.calendar.CalendarCreateRequest
import com.lovebird.api.dto.request.calendar.CalendarListRequest
import com.lovebird.api.dto.request.calendar.CalendarUpdateRequest
import com.lovebird.api.dto.response.calendar.CalendarDetailResponse
import com.lovebird.api.dto.response.calendar.CalendarListResponse
import com.lovebird.api.service.calendar.CalendarService
import com.lovebird.common.response.ApiResponse
import com.lovebird.domain.entity.User
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
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

	@GetMapping
	fun getCalendarsByMonth(
		@Valid @ModelAttribute
		calendarListRequest: CalendarListRequest,
		@AuthorizedUser user: User
	): ApiResponse<CalendarListResponse> {
		return ApiResponse.success(calendarService.findCalendarsByMonthAndUser(calendarListRequest.toParam(user)))
	}

	@PostMapping
	fun save(
		@AuthorizedUser user: User,
		@Valid @RequestBody
		request: CalendarCreateRequest
	): ResponseEntity<ApiResponse<Void>> {
		calendarService.save(request, user)
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success())
	}

	@PutMapping("/{id}")
	fun update(
		@PathVariable id: Long,
		@AuthorizedUser user: User,
		@Valid @RequestBody
		request: CalendarUpdateRequest
	): ApiResponse<Void> {
		calendarService.update(request.toParam(id, user))
		return ApiResponse.success()
	}

	@DeleteMapping("/{id}")
	fun delete(
		@PathVariable id: Long,
		@AuthorizedUser user: User
	): ApiResponse<Void> {
		calendarService.delete(id, user)
		return ApiResponse.success()
	}
}
