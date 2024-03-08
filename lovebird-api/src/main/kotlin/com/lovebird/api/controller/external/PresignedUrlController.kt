package com.lovebird.api.controller.external

import com.lovebird.api.annotation.AuthorizedUser
import com.lovebird.api.dto.request.external.DiaryUploadPresignedUrlRequest
import com.lovebird.api.dto.request.external.ProfileUploadPresignedUrlRequest
import com.lovebird.api.dto.response.external.PresignedUrlListResponse
import com.lovebird.api.dto.response.external.PresignedUrlResponse
import com.lovebird.api.service.external.PresignedUrlService
import com.lovebird.common.response.ApiResponse
import com.lovebird.domain.entity.User
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/presigned-urls")
class PresignedUrlController(
	private val presignedUrlService: PresignedUrlService
) {

	@PostMapping("/profile")
	fun getProfilePresignedUrl(
		@RequestBody request: ProfileUploadPresignedUrlRequest,
	): ApiResponse<PresignedUrlResponse> {
		return ApiResponse.success(presignedUrlService.getProfilePresignedUrl(request.toParam()))
	}

	@PostMapping("/diary")
	fun getDiaryPresignedUrls(
		@RequestBody request: DiaryUploadPresignedUrlRequest,
		@AuthorizedUser user: User
	): ApiResponse<PresignedUrlListResponse> {
		return ApiResponse.success(presignedUrlService.getDiaryPresignedUrls(request.toParam(user.id!!)))
	}
}
