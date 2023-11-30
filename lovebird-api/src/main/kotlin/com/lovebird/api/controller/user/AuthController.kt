package com.lovebird.api.controller.user

import com.lovebird.api.dto.request.user.SingUpRequest
import com.lovebird.api.service.user.AuthService
import com.lovebird.common.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
	private val authService: AuthService
) {

	@PostMapping("/sign-up/oidc")
	fun signUp(@RequestBody request: SingUpRequest.OidcUserRequest): ResponseEntity<ApiResponse<Void>> {
		authService.registerUserUsingOidc(request)

		return ApiResponse.created()
	}

	@PostMapping("/sign-up/naver")
	fun signUp(@RequestBody request: SingUpRequest.NaverUserRequest): ResponseEntity<ApiResponse<Void>> {
		authService.registerUserUsingNaver(request)

		return ApiResponse.created()
	}
}
