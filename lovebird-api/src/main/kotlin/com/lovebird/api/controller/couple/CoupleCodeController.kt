package com.lovebird.api.controller.couple

import com.lovebird.api.annotation.AuthorizedUser
import com.lovebird.api.dto.request.couple.CoupleLinkRequest
import com.lovebird.api.dto.response.couple.CoupleCodeResponse
import com.lovebird.api.dto.response.couple.CoupleLinkResponse
import com.lovebird.api.service.couple.CoupleCodeService
import com.lovebird.common.response.ApiResponse
import com.lovebird.domain.entity.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/couple")
class CoupleCodeController(
	private val coupleCodeService: CoupleCodeService
) {

	@GetMapping("/code")
	fun issueCoupleCode(@AuthorizedUser user: User): ApiResponse<CoupleCodeResponse> {
		return ApiResponse.success(coupleCodeService.generateCodeIfNotExist(user))
	}

	@PutMapping("/link")
	fun linkCouple(
		@AuthorizedUser user: User,
		@RequestBody request: CoupleLinkRequest
	): ApiResponse<CoupleLinkResponse> {
		return ApiResponse.success(coupleCodeService.linkCouple(request.toParam(user)))
	}
}
