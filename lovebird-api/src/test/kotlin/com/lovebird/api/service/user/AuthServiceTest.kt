package com.lovebird.api.service.user

import com.lovebird.api.common.base.ServiceDescribeSpec
import com.lovebird.api.dto.param.user.NaverLoginParam
import com.lovebird.api.dto.param.user.OAuthParam
import com.lovebird.api.dto.param.user.SignInParam
import com.lovebird.api.dto.request.user.SignUpRequest
import com.lovebird.api.dto.response.user.SignInResponse
import com.lovebird.api.dto.response.user.SignUpResponse
import com.lovebird.api.factory.AuthProviderFactory
import com.lovebird.api.provider.JwtProvider
import com.lovebird.api.service.couple.CoupleService
import com.lovebird.api.service.profile.ProfileService
import com.lovebird.api.utils.AuthTestFixture.getAccessTokenResponse
import com.lovebird.api.utils.CommonTestFixture.getPrincipalUser
import com.lovebird.api.validator.JwtValidator
import com.lovebird.api.vo.JwtToken
import com.lovebird.common.enums.Gender
import com.lovebird.common.enums.Provider
import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate

class AuthServiceTest : ServiceDescribeSpec({

	val userService: UserService = mockk<UserService>(relaxed = true)
	val profileService: ProfileService = mockk<ProfileService>(relaxed = true)
	val coupleService: CoupleService = mockk<CoupleService>(relaxed = true)
	val authFactory: AuthProviderFactory = mockk<AuthProviderFactory>(relaxed = true)
	val jwtProvider: JwtProvider = mockk<JwtProvider>(relaxed = true)
	val jwtValidator: JwtValidator = mockk<JwtValidator>(relaxed = true)
	val authService = AuthService(userService, profileService, coupleService, authFactory, jwtProvider, jwtValidator)

	afterTest {
		clearMocks(userService, profileService, coupleService, authFactory, jwtProvider, jwtValidator)
	}

	describe("signUpUserUsingOidc()") {
		val request = SignUpRequest.OidcUserRequest(
			provider = Provider.APPLE,
			deviceToken = "test-token",
			imageUrl = "test-image-url",
			email = "test-email",
			nickname = "test-nickname",
			birthday = LocalDate.of(1998, 5, 6),
			firstDate = LocalDate.of(2023, 12, 2),
			gender = Gender.MALE,
			idToken = "test-id-token"
		)
		val providerId = "123456789"
		val email = "lovebird@love.bird"
		val jwtToken = JwtToken("access token", "refresh token", "bearer")

		context("주어진 request 데이터가 정상 데이터일 때") {
			every { authFactory.getOAuthParam(request.provider, request.idToken) } returns OAuthParam(providerId, email)
			every { userService.findByProviderId(providerId) } returns null
			every { jwtProvider.generateJwtToken(any()) } returns jwtToken

			it("회원가입에 성공한다") {
				val response: SignUpResponse = authService.signUpUserUsingOidc(request)

				response shouldBe SignUpResponse.of(jwtToken)
			}
		}
		context("이미 존재하는 User 일 때") {
			every { authFactory.getOAuthParam(request.provider, request.idToken) } returns OAuthParam(providerId, email)
			every { userService.findByProviderId(providerId) } returns mockk(relaxed = true)

			it("회원가입에 실패한다") {
				shouldThrow<LbException> {
					authService.signUpUserUsingOidc(request)
				}
			}
		}
	}

	describe("signUpUserUsingNaver()") {
		val request = SignUpRequest.NaverUserRequest(
			provider = Provider.APPLE,
			deviceToken = "test-token",
			imageUrl = "test-image-url",
			email = "test-email",
			nickname = "test-nickname",
			birthday = LocalDate.of(1998, 5, 6),
			firstDate = LocalDate.of(2023, 12, 2),
			gender = Gender.MALE,
			code = "authorization-code",
			state = "lovebird"
		)
		val providerId = "123456789"
		val email = "lovebird@love.bird"
		val naverLoginParam = NaverLoginParam(request.code, request.state)
		val jwtToken = JwtToken("access token", "refresh token", "bearer")

		context("주어진 request 데이터가 정상 데이터일 때") {
			every { authFactory.getOAuthParam(request.provider, naverLoginParam) } returns OAuthParam(providerId, email)
			every { userService.findByProviderId(providerId) } returns null
			every { jwtProvider.generateJwtToken(any()) } returns jwtToken

			it("회원가입에 성공한다") {
				val response: SignUpResponse = authService.signUpUserUsingNaver(request)

				response shouldBe SignUpResponse.of(jwtToken)
			}
		}
		context("이미 존재하는 User 일 때") {
			every { authFactory.getOAuthParam(request.provider, naverLoginParam) } returns OAuthParam(providerId, email)
			every { userService.findByProviderId(providerId) } returns mockk(relaxed = true)

			it("회원가입에 실패한다") {
				shouldThrow<LbException> {
					authService.signUpUserUsingNaver(request)
				}
			}
		}
	}

	describe("signInUsingOidc()") {
		val providerId = "123456789"
		val param = SignInParam.OidcUserParam(
			provider = Provider.APPLE,
			idToken = "id-token"
		)
		val jwtToken = JwtToken("access token", "refresh token", "bearer")

		context("주어진 param 데이터가 정상 데이터일 때") {
			every { authFactory.getProviderId(param.provider, param.idToken) } returns providerId
			every { userService.findByProviderId(providerId) } returns mockk(relaxed = true)
			every { jwtProvider.generateJwtToken(any()) } returns jwtToken

			it("로그인에 성공한다") {
				val response: SignInResponse = authService.signInUsingOidc(param)

				response shouldBe SignInResponse.of(jwtToken, false)
			}
		}
		context("해당 providerId의 User가 존재하지 않을 때") {
			every { authFactory.getProviderId(param.provider, param.idToken) } returns providerId
			every { userService.findByProviderId(providerId) } returns null

			it("예외를 던진다") {
				shouldThrow<LbException> { authService.signInUsingOidc(param) }
			}
		}
	}

	describe("signInUsingNaver()") {
		val providerId = "123456789"
		val param = SignInParam.NaverUserParam(
			provider = Provider.APPLE,
			code = "authorization-code",
			state = "lovebird"
		)
		val jwtToken = JwtToken("access token", "refresh token", "bearer")

		context("주어진 param 데이터가 정상 데이터일 때") {
			every { authFactory.getProviderId(param.provider, NaverLoginParam(param.code, param.state)) } returns providerId
			every { userService.findByProviderId(providerId) } returns mockk(relaxed = true)
			every { jwtProvider.generateJwtToken(any()) } returns jwtToken

			it("로그인에 성공한다") {
				val response: SignInResponse = authService.signInUsingNaver(param)

				response shouldBe SignInResponse.of(jwtToken, false)
			}
		}
		context("해당 providerId의 User가 존재하지 않을 때") {
			every { authFactory.getProviderId(param.provider, NaverLoginParam(param.code, param.state)) } returns providerId
			every { userService.findByProviderId(providerId) } returns null

			it("예외를 던진다") {
				shouldThrow<LbException> { authService.signInUsingNaver(param) }
			}
		}
	}

	describe("recreateAccessToken()") {
		val refreshToken = "refresh-token"
		val principalUser = getPrincipalUser(1, "123456789")
		val accessToken = getAccessTokenResponse()

		context("올바른 Refresh Token 일 때") {
			every { jwtValidator.getPrincipalUser(refreshToken) } returns principalUser
			every { jwtProvider.generateAccessToken(principalUser) } returns accessToken

			it("AccessToken 을 재발급한다") {
				authService.recreateAccessToken(refreshToken).should {
					accessToken
				}

				verify(exactly = 1) {
					jwtValidator.getPrincipalUser(refreshToken)
					jwtProvider.generateAccessToken(principalUser)
				}
			}
		}
		context("잘못된 Refresh Token 일 때") {
			every { jwtValidator.getPrincipalUser(refreshToken) } throws LbException(ReturnCode.WRONG_JWT_TOKEN)

			it("예외가 발생한다") {
				val exception = shouldThrow<LbException> {
					authService.recreateAccessToken(refreshToken)
				}

				exception.getMsg().should { ReturnCode.WRONG_JWT_TOKEN.message }
				exception.getCode().should { ReturnCode.WRONG_JWT_TOKEN.code }

				verify(exactly = 1) { jwtValidator.getPrincipalUser(refreshToken) }
				verify(exactly = 0) { jwtProvider.generateAccessToken(principalUser) }
			}
		}
	}
})
