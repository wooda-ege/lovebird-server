package com.lovebird.api.controller.user

import com.lovebird.api.annotation.AuthorizedUser
import com.lovebird.api.annotation.RefreshToken
import com.lovebird.api.dto.request.user.SignInRequest
import com.lovebird.api.dto.request.user.SignUpRequest
import com.lovebird.api.dto.response.user.AccessTokenResponse
import com.lovebird.api.dto.response.user.SignInResponse
import com.lovebird.api.dto.response.user.SignUpResponse
import com.lovebird.api.service.user.AuthProcessingService
import com.lovebird.api.service.user.AuthService
import com.lovebird.api.service.user.SuperAuthService
import com.lovebird.common.response.ApiResponse
import com.lovebird.domain.entity.User
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
	private val authService: AuthService,
	private val superAuthService: SuperAuthService,
	private val authProcessingService: AuthProcessingService
) {

	@PostMapping("/sign-up/oidc")
	fun signUp(@RequestBody request: SignUpRequest.OidcUserRequest): ApiResponse<SignUpResponse> {
		val response: SignUpResponse = authService.signUpUserUsingOidc(request)

		return ApiResponse.success(response)
	}

	@PostMapping("/sign-up/naver")
	fun signUp(@RequestBody request: SignUpRequest.NaverUserRequest): ApiResponse<SignUpResponse> {
		val response: SignUpResponse = authService.signUpUserUsingNaver(request)

		return ApiResponse.success(response)
	}

	@PostMapping("/sign-in/oidc")
	fun signIn(@RequestBody request: SignInRequest.OidcUserRequest): ApiResponse<SignInResponse> {
		return ApiResponse.success(authService.signInUsingOidc(request.toParam()))
	}

	@PostMapping("/sign-in/naver")
	fun signIn(@RequestBody request: SignInRequest.NaverUserRequest): ApiResponse<SignInResponse> {
		return ApiResponse.success(authService.signInUsingNaver(request.toParam()))
	}

	@PostMapping("/sign-in/super/{id}")
	fun signInSuper(@PathVariable id: Long): ApiResponse<SignInResponse> {
		return ApiResponse.success(superAuthService.signInSuper(id))
	}

	@PostMapping("/access-token")
	fun regenerateAccessToken(@RefreshToken refreshToken: String): ApiResponse<AccessTokenResponse> {
		return ApiResponse.success(authService.recreateAccessToken(refreshToken))
	}

	@DeleteMapping
	fun deleteAccount(@AuthorizedUser user: User): ApiResponse<Void> {
		authProcessingService.deleteAccount(user)
		return ApiResponse.success()
	}
}
