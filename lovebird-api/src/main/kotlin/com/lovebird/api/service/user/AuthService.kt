package com.lovebird.api.service.user

import com.lovebird.api.dto.param.auth.UserAuthParam
import com.lovebird.api.dto.param.auth.UserRegisterParam
import com.lovebird.api.dto.request.user.SingUpRequest
import com.lovebird.api.dto.response.user.SignUpResponse
import com.lovebird.api.service.profile.ProfileService
import com.lovebird.common.enums.Provider
import com.lovebird.domain.entity.User
import com.lovebird.security.dto.param.NaverLoginParam
import com.lovebird.security.dto.param.OAuthParam
import com.lovebird.security.factory.AuthProviderFactory
import com.lovebird.security.provider.jwt.JwtProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
	private val userService: UserService,
	private val profileService: ProfileService,
	private val authProviderFactory: AuthProviderFactory,
	private val jwtProvider: JwtProvider
) {
	@Transactional
	fun registerUserUsingOidc(request: SingUpRequest.OidcUserRequest): SignUpResponse {
		val param = signUpUsingOidc(request.toUserRegisterParam())
		profileService.save(request.toProfileCreateParam(param.user))

		return SignUpResponse.of(jwtProvider.generateJwtToken(param.principalUser))
	}

	@Transactional
	fun registerUserUsingNaver(request: SingUpRequest.NaverUserRequest): SignUpResponse {
		val param = signUpUsingNaver(request.toUserRegisterParam())
		profileService.save(request.toProfileCreateParam(param.user))

		return SignUpResponse.of(jwtProvider.generateJwtToken(param.principalUser))
	}

	private fun signUpUsingOidc(param: UserRegisterParam.OidcUserParam): UserAuthParam {
		val oAuthParam: OAuthParam = getOAuthParam(param.provider, param.idToken)
		val user: User = userService.save(param.toUserEntity(oAuthParam.providerId))

		return UserAuthParam.of(user)
	}

	private fun signUpUsingNaver(param: UserRegisterParam.NaverUserParam): UserAuthParam {
		val oAuthParam: OAuthParam = getOAuthParam(param.provider, NaverLoginParam(param.code, param.state))
		val user: User = userService.save(param.toUserEntity(oAuthParam.providerId))

		return UserAuthParam.of(user)
	}

	private fun getOAuthParam(provider: Provider, request: Any): OAuthParam {
		return authProviderFactory.getOAuthParam(provider, request)
	}
}
