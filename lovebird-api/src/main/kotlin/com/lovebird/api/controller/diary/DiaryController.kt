package com.lovebird.api.controller.diary

import com.lovebird.api.annotation.AuthorizedUser
import com.lovebird.api.dto.request.diary.DiaryCreateRequest
import com.lovebird.api.dto.request.diary.DiaryListRequest
import com.lovebird.api.dto.request.diary.DiaryUpdateRequest
import com.lovebird.api.dto.response.diary.DiaryDetailResponse
import com.lovebird.api.dto.response.diary.DiaryListResponse
import com.lovebird.api.dto.response.diary.DiarySimpleResponse
import com.lovebird.api.service.diary.DiaryService
import com.lovebird.common.response.ApiResponse
import com.lovebird.domain.entity.User
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/diaries")
@RestController
class DiaryController(
	private val diaryService: DiaryService
) {

	@PostMapping
	fun save(
		@AuthorizedUser user: User,
		@RequestBody request: DiaryCreateRequest
	): ApiResponse<Unit> {
		diaryService.save(request.toParam(user))
		return ApiResponse.success()
	}

	@GetMapping("/memory-date")
	fun findAllByMemoryDate(
		@AuthorizedUser user: User,
		@ModelAttribute request: DiaryListRequest.SearchByMemoryDateRequest
	): ApiResponse<DiarySimpleResponse> {
		return ApiResponse.success(diaryService.findAllByMemoryDate(request, user))
	}

	@GetMapping
	fun findAll(
		@AuthorizedUser user: User
	): ApiResponse<DiarySimpleResponse> {
		return ApiResponse.success(diaryService.findAll(user))
	}

	@GetMapping("/cursor")
	fun findAllByCursor(
		@AuthorizedUser user: User,
		@ModelAttribute request: DiaryListRequest.SearchByCursorRequest
	): ApiResponse<DiaryListResponse> {
		return ApiResponse.success(diaryService.findPageByCursor(request, user))
	}

	@GetMapping("/{diaryId}")
	fun findDetailById(@PathVariable diaryId: Long): ApiResponse<DiaryDetailResponse> {
		return ApiResponse.success(diaryService.findDetailById(diaryId))
	}

	@PutMapping("/{diaryId}")
	fun modify(
		@PathVariable diaryId: Long,
		@RequestBody request: DiaryUpdateRequest
	): ApiResponse<Unit> {
		diaryService.update(request.toParam(diaryId))
		return ApiResponse.success()
	}

	@DeleteMapping("/{diaryId}")
	fun delete(@PathVariable diaryId: Long): ApiResponse<Unit> {
		diaryService.delete(diaryId)
		return ApiResponse.success()
	}
}
