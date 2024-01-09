package com.lovebird.api.service.user

import com.lovebird.api.dto.param.user.NaverLoginParam
import com.lovebird.api.dto.param.user.OAuthParam
import com.lovebird.api.dto.param.user.SignInParam
import com.lovebird.api.dto.param.user.SignUpParam
import com.lovebird.api.dto.param.user.UserAuthParam
import com.lovebird.api.dto.request.user.SignUpRequest
import com.lovebird.api.dto.response.user.SignInResponse
import com.lovebird.api.dto.response.user.SignUpResponse
import com.lovebird.api.factory.AuthProviderFactory
import com.lovebird.api.provider.JwtProvider
import com.lovebird.api.service.couple.CoupleService
import com.lovebird.api.service.profile.ProfileService
import com.lovebird.api.vo.PrincipalUser
import com.lovebird.common.enums.Provider
import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.lovebird.domain.entity.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
	private val userService: UserService,
	private val profileService: ProfileService,
	private val coupleService: CoupleService,
	private val authProviderFactory: AuthProviderFactory,
	private val jwtProvider: JwtProvider
) {
	@Transactional
	fun signUpUserUsingOidc(request: SignUpRequest.OidcUserRequest): SignUpResponse {
		val param = signUsingOidc(request.toUserRegisterParam())
		profileService.save(request.toProfileCreateParam(param.user))

		return SignUpResponse.of(jwtProvider.generateJwtToken(param.principalUser))
	}

	@Transactional
	fun signUpUserUsingNaver(request: SignUpRequest.NaverUserRequest): SignUpResponse {
		val param = signUsingNaver(request.toUserRegisterParam())
		profileService.save(request.toProfileCreateParam(param.user))

		return SignUpResponse.of(jwtProvider.generateJwtToken(param.principalUser))
	}

	@Transactional(readOnly = true)
	fun signInUsingOidc(param: SignInParam.OidcUserParam): SignInResponse {
		val user = findUserByProviderId(getProviderId(param.provider, param.idToken))
		val jwtToken = jwtProvider.generateJwtToken(PrincipalUser.of(user))

		return SignInResponse.of(jwtToken, coupleService.existByUser(user))
	}

	@Transactional(readOnly = true)
	fun signInUsingNaver(param: SignInParam.NaverUserParam): SignInResponse {
		val user = findUserByProviderId(getProviderId(param.provider, NaverLoginParam(param.code, param.state)))
		val jwtToken = jwtProvider.generateJwtToken(PrincipalUser.of(user))

		return SignInResponse.of(jwtToken, coupleService.existByUser(user))
	}

	private fun signUsingOidc(param: SignUpParam.OidcUserParam): UserAuthParam {
		val oAuthParam: OAuthParam = getOAuthParam(param.provider, param.idToken)
		validateProviderId(oAuthParam.providerId)
		val user: User = userService.save(param.toUserEntity(oAuthParam.providerId))

		return UserAuthParam.of(user)
	}

	private fun signUsingNaver(param: SignUpParam.NaverUserParam): UserAuthParam {
		val oAuthParam: OAuthParam = getOAuthParam(param.provider, NaverLoginParam(param.code, param.state))
		validateProviderId(oAuthParam.providerId)
		val user: User = userService.save(param.toUserEntity(oAuthParam.providerId))

		return UserAuthParam.of(user)
	}

	private fun validateProviderId(providerId: String) {
		if (userService.findByProviderId(providerId) != null) {
			throw LbException(ReturnCode.DUPLICATE_SIGN_UP)
		}
	}

	private fun findUserByProviderId(providerId: String): User {
		return userService.findByProviderId(providerId) ?: throw LbException(ReturnCode.NOT_EXIST_USER)
	}

	private fun getOAuthParam(provider: Provider, request: Any): OAuthParam {
		return authProviderFactory.getOAuthParam(provider, request)
	}

	private fun getProviderId(provider: Provider, request: Any): String {
		return authProviderFactory.getProviderId(provider, request)
	}
}
