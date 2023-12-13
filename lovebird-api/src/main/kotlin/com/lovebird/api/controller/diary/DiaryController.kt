package com.lovebird.api.controller.diary

import com.lovebird.api.dto.request.diary.DiaryCreateRequest
import com.lovebird.api.dto.request.diary.DiaryListRequest
import com.lovebird.api.dto.request.diary.DiaryUpdateRequest
import com.lovebird.api.dto.response.diary.DiaryDetailResponse
import com.lovebird.api.dto.response.diary.DiarySimpleListResponse
import com.lovebird.api.service.diary.DiaryService
import com.lovebird.common.response.ApiResponse
import com.lovebird.domain.entity.User
import com.lovebird.security.annotation.AuthorizedUser
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

@RequestMapping("/api/v1/diaries")
@RestController
class DiaryController(
	private val diaryService: DiaryService
) {

	@PostMapping
	fun save(
		@AuthorizedUser user: User,
		@RequestBody request: DiaryCreateRequest
	): ResponseEntity<ApiResponse<Void>> {
		diaryService.save(request.toParam(user))
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success())
	}

	@GetMapping
	fun findAllByMemoryDate(
		@AuthorizedUser user: User,
		@ModelAttribute request: DiaryListRequest.SearchByMemoryDateRequest
	): ApiResponse<DiarySimpleListResponse> {
		return ApiResponse.success(diaryService.findAllByMemoryDate(request, user))
	}

	@GetMapping("/{id}")
	fun findDetailById(@PathVariable id: Long): ApiResponse<DiaryDetailResponse> {
		return ApiResponse.success(diaryService.findDetailById(id))
	}

	@PutMapping("/{id}")
	fun modify(
		@PathVariable id: Long,
		@RequestBody request: DiaryUpdateRequest
	): ApiResponse<Void> {
		diaryService.update(request.toParam())
		return ApiResponse.success()
	}

	@DeleteMapping("/{id}")
	fun delete(@PathVariable id: Long): ApiResponse<Void> {
		diaryService.delete(id)
		return ApiResponse.success()
	}
}
