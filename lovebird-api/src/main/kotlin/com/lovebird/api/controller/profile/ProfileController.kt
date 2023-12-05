package com.lovebird.api.controller.profile

import com.lovebird.api.dto.request.profile.ProfileUpdateRequest
import com.lovebird.api.dto.response.profile.ProfileDetailResponse
import com.lovebird.api.service.profile.ProfileService
import com.lovebird.common.annotation.AuthorizedUser
import com.lovebird.common.response.ApiResponse
import com.lovebird.domain.entity.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/profile")
class ProfileController(
	private val profileService: ProfileService
) {

	@GetMapping
	fun getProfile(@AuthorizedUser user: User): ApiResponse<ProfileDetailResponse> {
		return ApiResponse.success(profileService.findDetailByUser(user))
	}

	@PutMapping
	fun modifyProfile(
		@AuthorizedUser user: User,
		@RequestBody request: ProfileUpdateRequest
	): ApiResponse<Void> {
		profileService.update(user, request.toParam())
		return ApiResponse.success()
	}
}
