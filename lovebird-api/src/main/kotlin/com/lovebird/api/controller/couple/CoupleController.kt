package com.lovebird.api.controller.couple

import com.lovebird.api.dto.response.couple.CoupleCheckResponse
import com.lovebird.api.service.couple.CoupleService
import com.lovebird.common.response.ApiResponse
import com.lovebird.domain.entity.User
import com.lovebird.security.annotation.AuthorizedUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/couple")
class CoupleController(
	private val coupleService: CoupleService
) {

	@GetMapping("/check")
	fun checkCouple(@AuthorizedUser user: User): ApiResponse<CoupleCheckResponse> {
		return ApiResponse.success(CoupleCheckResponse(coupleService.existByUser(user)))
	}
}
