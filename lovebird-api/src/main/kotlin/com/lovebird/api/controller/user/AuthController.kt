package com.lovebird.api.controller.user

import com.lovebird.api.dto.request.user.SignInRequest
import com.lovebird.api.dto.request.user.SingUpRequest
import com.lovebird.api.dto.response.user.SignInResponse
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
		authService.signUpUserUsingOidc(request)

		return ApiResponse.created()
	}

	@PostMapping("/sign-up/naver")
	fun signUp(@RequestBody request: SingUpRequest.NaverUserRequest): ResponseEntity<ApiResponse<Void>> {
		authService.signUpUserUsingNaver(request)

		return ApiResponse.created()
	}

	@PostMapping("/sign-in/oidc")
	fun signIn(@RequestBody request: SignInRequest.OidcUserRequest): ApiResponse<SignInResponse> {
		return ApiResponse.success(authService.signInUsingOidc(request.toParam()))
	}

	@PostMapping("/sign-in/naver")
	fun signIn(@RequestBody request: SignInRequest.NaverUserRequest): ApiResponse<SignInResponse> {
		return ApiResponse.success(authService.signInUsingNaver(request.toParam()))
	}
}
