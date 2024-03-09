package com.lovebird.api.service.user

import com.lovebird.api.dto.response.user.SignInResponse
import com.lovebird.api.provider.JwtProvider
import com.lovebird.api.service.couple.CoupleService
import com.lovebird.api.vo.JwtToken
import com.lovebird.api.vo.PrincipalUser
import com.lovebird.domain.entity.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SuperAuthService(
	private val userService: UserService,
	private val coupleService: CoupleService,
	private val jwtProvider: JwtProvider
) {

	@Transactional
	fun signInSuper(id: Long): SignInResponse {
		val superUser: User = userService.findById(id)
		val jwtToken: JwtToken = jwtProvider.generateJwtToken(PrincipalUser.from(superUser))

		return SignInResponse.of(jwtToken, coupleService.existByUser(superUser))
	}
}
