package com.lovebird.api.controller.user

import com.lovebird.api.common.base.ControllerDescribeSpec
import com.lovebird.api.dto.request.user.SignInRequest
import com.lovebird.api.dto.request.user.SignUpRequest
import com.lovebird.api.dto.response.user.SignInResponse
import com.lovebird.api.dto.response.user.SignUpResponse
import com.lovebird.api.service.user.AuthDeleteService
import com.lovebird.api.service.user.AuthService
import com.lovebird.api.service.user.SuperAuthService
import com.lovebird.api.utils.AuthTestFixture.getAccessTokenResponse
import com.lovebird.api.utils.andExpectData
import com.lovebird.api.utils.restdocs.BOOLEAN
import com.lovebird.api.utils.restdocs.STRING
import com.lovebird.api.utils.restdocs.andDocument
import com.lovebird.api.utils.restdocs.headerMeans
import com.lovebird.api.utils.restdocs.requestBody
import com.lovebird.api.utils.restdocs.requestHeaders
import com.lovebird.api.utils.restdocs.restDocMockMvcBuild
import com.lovebird.api.utils.restdocs.type
import com.lovebird.api.utils.shouldBe
import com.lovebird.common.enums.Gender
import com.lovebird.common.enums.Provider
import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.request
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext
import java.time.LocalDate

@WebMvcTest(AuthController::class)
class AuthControllerTest(
	@MockkBean(relaxed = true)
	private val authService: AuthService,
	@MockkBean(relaxed = true)
	private val superAuthService: SuperAuthService,
	@MockkBean(relaxed = true)
	private val authDeleteService: AuthDeleteService,
	@Autowired
	private val context: WebApplicationContext
) : ControllerDescribeSpec({

	val baseUrl = "/api/v1/auth"
	val restDocumentation = ManualRestDocumentation()
	val mockMvc = restDocMockMvcBuild(context, restDocumentation)

	beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
	afterEach { restDocumentation.afterTest() }

	describe("POST : /api/v1/auth/sign-up/oidc") {
		val url = "$baseUrl/sign-up/oidc"
		val requestBody = SignUpRequest.OidcUserRequest(
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
		val response = SignUpResponse("access token", "refresh token")

		context("유효한 요청이 전달 되면") {
			val requestJson = toJson(requestBody)
			val request = request(HttpMethod.POST, url)
				.contentType(APPLICATION_JSON)
				.content(requestJson)

			it("1000 SUCCESS") {
				every { authService.signUpUserUsingOidc(any()) } returns response

				mockMvc.perform(request)
					.andExpect(status().isOk)
					.andExpectData(
						jsonPath("$.code") shouldBe ReturnCode.SUCCESS.code,
						jsonPath("$.message") shouldBe ReturnCode.SUCCESS.message,
						jsonPath("$.data.accessToken") shouldBe response.accessToken,
						jsonPath("$.data.refreshToken") shouldBe response.refreshToken
					)
					.andDocument(
						"1000-sign-up-oidc",
						requestBody(
							"provider" type STRING means "소셜 제공자",
							"deviceToken" type STRING means "디바이스 토큰" isOptional true,
							"imageUrl" type STRING means "프로필 이미지 URL",
							"email" type STRING means "이메일",
							"nickname" type STRING means "닉네임",
							"birthday" type STRING means "생일" isOptional true,
							"firstDate" type STRING means "사귄 날짜" isOptional true,
							"gender" type STRING means "성별",
							"idToken" type STRING means "소셜 로그인 토큰"
						),
						envelopeResponseBody(
							"data.accessToken" type STRING means "액세스 토큰",
							"data.refreshToken" type STRING means "리프레시 토큰"
						)
					)
			}
		}
		context("이미 가입한 유저일 때") {
			val requestJson = toJson(requestBody)
			val request = request(HttpMethod.POST, url)
				.contentType(APPLICATION_JSON)
				.content(requestJson)

			it("1201 DUPLICATE_SIGN_UP") {
				every { authService.signUpUserUsingOidc(any()) } throws LbException(ReturnCode.DUPLICATE_SIGN_UP)

				mockMvc.perform(request)
					.andExpect(status().isBadRequest)
					.andExpectData(
						jsonPath("$.code") shouldBe ReturnCode.DUPLICATE_SIGN_UP.code,
						jsonPath("$.message") shouldBe ReturnCode.DUPLICATE_SIGN_UP.message
					)
					.andDocument(
						"1201-sign-up-oidc",
						requestBody(
							"provider" type STRING means "소셜 제공자",
							"deviceToken" type STRING means "디바이스 토큰" isOptional true,
							"imageUrl" type STRING means "프로필 이미지 URL",
							"email" type STRING means "이메일",
							"nickname" type STRING means "닉네임",
							"birthday" type STRING means "생일" isOptional true,
							"firstDate" type STRING means "사귄 날짜" isOptional true,
							"gender" type STRING means "성별",
							"idToken" type STRING means "소셜 로그인 토큰"
						),
						envelopeResponseBody(dataOptional = true)
					)
			}
		}
	}

	describe("POST : /api/v1/auth/sign-up/naver") {
		val url = "$baseUrl/sign-up/naver"
		val requestBody = SignUpRequest.NaverUserRequest(
			provider = Provider.NAVER,
			deviceToken = "test-token",
			imageUrl = "test-image-url",
			email = "test-email",
			nickname = "test-nickname",
			birthday = LocalDate.of(1998, 5, 6),
			firstDate = LocalDate.of(2023, 12, 2),
			gender = Gender.MALE,
			code = "test-authorization-token",
			state = "lovebird"
		)
		val response = SignUpResponse("access token", "refresh token")

		context("유효한 요청이 전달 되면") {
			val requestJson = toJson(requestBody)
			val request = request(HttpMethod.POST, url)
				.contentType(APPLICATION_JSON)
				.content(requestJson)

			it("1000 SUCCESS") {
				every { authService.signUpUserUsingNaver(any()) } returns response

				mockMvc.perform(request)
					.andExpect(status().isOk)
					.andExpectData(
						jsonPath("$.code") shouldBe ReturnCode.SUCCESS.code,
						jsonPath("$.message") shouldBe ReturnCode.SUCCESS.message,
						jsonPath("$.data.accessToken") shouldBe response.accessToken,
						jsonPath("$.data.refreshToken") shouldBe response.refreshToken
					)
					.andDocument(
						"1000-sign-up-naver",
						requestBody(
							"provider" type STRING means "소셜 제공자",
							"deviceToken" type STRING means "디바이스 토큰" isOptional true,
							"imageUrl" type STRING means "프로필 이미지 URL",
							"email" type STRING means "이메일",
							"nickname" type STRING means "닉네임",
							"birthday" type STRING means "생일" isOptional true,
							"firstDate" type STRING means "사귄 날짜" isOptional true,
							"gender" type STRING means "성별",
							"code" type STRING means "인가 코드",
							"state" type STRING means "상태"
						),
						envelopeResponseBody(
							"data.accessToken" type STRING means "액세스 토큰",
							"data.refreshToken" type STRING means "리프레시 토큰"
						)
					)
			}
		}
		context("이미 가입한 유저일 때") {
			val requestJson = toJson(requestBody)
			val request = request(HttpMethod.POST, url)
				.contentType(APPLICATION_JSON)
				.content(requestJson)

			it("1201 DUPLICATE_SIGN_UP") {
				every { authService.signUpUserUsingNaver(any()) } throws LbException(ReturnCode.DUPLICATE_SIGN_UP)

				mockMvc.perform(request)
					.andExpect(status().isBadRequest)
					.andExpectData(
						jsonPath("$.code") shouldBe ReturnCode.DUPLICATE_SIGN_UP.code,
						jsonPath("$.message") shouldBe ReturnCode.DUPLICATE_SIGN_UP.message
					)
					.andDocument(
						"1201-sign-up-naver",
						requestBody(
							"provider" type STRING means "소셜 제공자",
							"deviceToken" type STRING means "디바이스 토큰" isOptional true,
							"imageUrl" type STRING means "프로필 이미지 URL",
							"email" type STRING means "이메일",
							"nickname" type STRING means "닉네임",
							"birthday" type STRING means "생일" isOptional true,
							"firstDate" type STRING means "사귄 날짜" isOptional true,
							"gender" type STRING means "성별",
							"code" type STRING means "인가 코드",
							"state" type STRING means "상태"
						),
						envelopeResponseBody()
					)
			}
		}
	}

	describe("POST : /api/v1/auth/sign-in/oidc") {
		val url = "$baseUrl/sign-in/oidc"
		val requestBody = SignInRequest.OidcUserRequest(
			provider = Provider.APPLE,
			idToken = "test-id-token"
		)
		val response = SignInResponse("access token", "refresh token", true)

		context("유효한 요청이 전달 되면") {
			val requestJson = toJson(requestBody)
			val request = request(HttpMethod.POST, url)
				.contentType(APPLICATION_JSON)
				.content(requestJson)

			it("1000 SUCCESS") {
				every { authService.signInUsingOidc(any()) } returns response

				mockMvc.perform(request)
					.andExpect(status().isOk)
					.andExpectData(
						jsonPath("$.code") shouldBe ReturnCode.SUCCESS.code,
						jsonPath("$.message") shouldBe ReturnCode.SUCCESS.message,
						jsonPath("$.data.accessToken") shouldBe response.accessToken,
						jsonPath("$.data.refreshToken") shouldBe response.refreshToken
					)
					.andDocument(
						"1000-sign-in-oidc",
						requestBody(
							"provider" type STRING means "소셜 제공자",
							"idToken" type STRING means "소셜 로그인 토큰"
						),
						envelopeResponseBody(
							"data.accessToken" type STRING means "액세스 토큰",
							"data.refreshToken" type STRING means "리프레시 토큰",
							"data.linkedFlag" type BOOLEAN means "커플 연동 여부"
						)
					)
			}
		}
		context("가입하지 않은 유저일 때") {
			val requestJson = toJson(requestBody)
			val request = request(HttpMethod.POST, url)
				.contentType(APPLICATION_JSON)
				.content(requestJson)

			it("1202 NOT_EXIST_USER") {
				every { authService.signInUsingOidc(any()) } throws LbException(ReturnCode.NOT_EXIST_USER)

				mockMvc.perform(request)
					.andExpect(status().isBadRequest)
					.andExpectData(
						jsonPath("$.code") shouldBe ReturnCode.NOT_EXIST_USER.code,
						jsonPath("$.message") shouldBe ReturnCode.NOT_EXIST_USER.message
					)
					.andDocument(
						"1202-sign-in-oidc",
						requestBody(
							"provider" type STRING means "소셜 제공자",
							"idToken" type STRING means "소셜 로그인 토큰"
						),
						envelopeResponseBody()
					)
			}
		}
	}

	describe("POST : /api/v1/auth/sign-in/naver") {
		val url = "$baseUrl/sign-in/naver"
		val requestBody = SignInRequest.NaverUserRequest(
			provider = Provider.NAVER,
			code = "test-authorization-token",
			state = "lovebird"
		)
		val response = SignInResponse("access token", "refresh token", true)

		context("유효한 요청이 전달 되면") {
			val requestJson = toJson(requestBody)
			val request = request(HttpMethod.POST, url)
				.contentType(APPLICATION_JSON)
				.content(requestJson)

			it("1000 SUCCESS") {
				every { authService.signInUsingNaver(any()) } returns response

				mockMvc.perform(request)
					.andExpect(status().isOk)
					.andExpectData(
						jsonPath("$.code") shouldBe ReturnCode.SUCCESS.code,
						jsonPath("$.message") shouldBe ReturnCode.SUCCESS.message,
						jsonPath("$.data.accessToken") shouldBe response.accessToken,
						jsonPath("$.data.refreshToken") shouldBe response.refreshToken
					)
					.andDocument(
						"1000-sign-in-naver",
						requestBody(
							"provider" type STRING means "소셜 제공자",
							"code" type STRING means "인가 코드",
							"state" type STRING means "상태"
						),
						envelopeResponseBody(
							"data.accessToken" type STRING means "액세스 토큰",
							"data.refreshToken" type STRING means "리프레시 토큰",
							"data.linkedFlag" type BOOLEAN means "커플 연동 여부"
						)
					)
			}
		}
		context("가입하지 않은 유저일 때") {
			val requestJson = toJson(requestBody)
			val request = request(HttpMethod.POST, url)
				.contentType(APPLICATION_JSON)
				.content(requestJson)

			it("1202 NOT_EXIST_USER") {
				every { authService.signInUsingNaver(any()) } throws LbException(ReturnCode.NOT_EXIST_USER)

				mockMvc.perform(request)
					.andExpect(status().isBadRequest)
					.andExpectData(
						jsonPath("$.code") shouldBe ReturnCode.NOT_EXIST_USER.code,
						jsonPath("$.message") shouldBe ReturnCode.NOT_EXIST_USER.message
					)
					.andDocument(
						"1202-sign-in-naver",
						requestBody(
							"provider" type STRING means "소셜 제공자",
							"code" type STRING means "인가 코드",
							"state" type STRING means "상태"
						),
						envelopeResponseBody()
					)
			}
		}
	}

	describe("POST : /api/v1/auth/recreate-access-token") {
		val url = "$baseUrl/access-token"
		val refreshToken = "refresh-token"

		context("정상적인 Refresh Token 일 때") {
			val request = request(HttpMethod.POST, url)
				.header("Refresh", refreshToken)

			it("1000 SUCCESS") {
				every { authService.recreateAccessToken(refreshToken) } returns getAccessTokenResponse()

				mockMvc.perform(request)
					.andExpect(status().isOk)
					.andExpectData(
						jsonPath("$.code") shouldBe ReturnCode.SUCCESS.code,
						jsonPath("$.message") shouldBe ReturnCode.SUCCESS.message,
						jsonPath("$.data.accessToken") shouldBe "access-token"
					)
					.andDocument(
						"1000-recreate-access-token",
						requestHeaders(
							"Refresh" headerMeans "리프레시 토큰"
						),
						envelopeResponseBody(
							"data.accessToken" type STRING means "액세스 토큰"
						)
					)
			}
		}
	}

	describe("DELETE: /api/v1/auth") {
		context("로그인한 유저라면") {
			val request = request(HttpMethod.DELETE, baseUrl)
				.header(HttpHeaders.AUTHORIZATION, "Bearer access-token")

			it("1000 SUCCESS") {
				every { authDeleteService.deleteAccount(any()) } returns Unit

				mockMvc.perform(request)
					.andExpect(status().isOk)
					.andDocument(
						"1000-auth-delete-account",
						requestHeaders(
							"Authorization" headerMeans "액세스 토큰"
						),
						envelopeResponseBody(dataOptional = true)
					)
			}
		}
	}
})
