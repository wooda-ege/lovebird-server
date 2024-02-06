package com.lovebird.api.controller.couple

import com.lovebird.api.common.base.ControllerDescribeSpec
import com.lovebird.api.dto.request.couple.CoupleLinkRequest
import com.lovebird.api.dto.response.couple.CoupleCodeResponse
import com.lovebird.api.dto.response.couple.CoupleLinkResponse
import com.lovebird.api.service.couple.CoupleCodeService
import com.lovebird.api.utils.andExpectData
import com.lovebird.api.utils.restdocs.NUMBER
import com.lovebird.api.utils.restdocs.STRING
import com.lovebird.api.utils.restdocs.andDocument
import com.lovebird.api.utils.restdocs.headerMeans
import com.lovebird.api.utils.restdocs.requestBody
import com.lovebird.api.utils.restdocs.requestHeaders
import com.lovebird.api.utils.restdocs.restDocMockMvcBuild
import com.lovebird.api.utils.restdocs.type
import com.lovebird.api.utils.shouldBe
import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.request
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext

@WebMvcTest(CoupleCodeController::class)
class CoupleCodeControllerTest(
	@MockkBean(relaxed = true)
	private val coupleCodeService: CoupleCodeService,
	@Autowired
	private val context: WebApplicationContext
) : ControllerDescribeSpec({

	val baseUrl = "/api/v1/couple"
	val restDocumentation = ManualRestDocumentation()
	val mockMvc = restDocMockMvcBuild(context, restDocumentation)

	beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
	afterEach { restDocumentation.afterTest() }

	describe("POST : /api/v1/couple/code") {
		val url = "$baseUrl/code"
		context("유효한 요청이 전달 되면") {
			val response = getCoupleCodeResponse()
			val request = request(HttpMethod.POST, url)
				.header(AUTHORIZATION, "Bearer access-token")

			it("1000 SUCCESS") {
				every { coupleCodeService.generateCodeIfNotExist(any()) } returns response

				mockMvc.perform(request)
					.andExpect(status().isOk)
					.andExpectData(
						jsonPath("$.code") shouldBe ReturnCode.SUCCESS.code,
						jsonPath("$.message") shouldBe ReturnCode.SUCCESS.message,
						jsonPath("$.data.coupleCode") shouldBe response.coupleCode,
						jsonPath("$.data.remainSeconds") shouldBe response.remainSeconds
					)
					.andDocument(
						"1000-get-couple-code",
						requestHeaders(
							"Authorization" headerMeans "액세스 토큰"
						),
						envelopeResponseBody(
							"data.coupleCode" type STRING means "커플 코드",
							"data.remainSeconds" type NUMBER means "남은 시간(초)"
						)
					)
			}
		}
	}

	describe("POST : /api/v1/couple/link") {
		val url = "$baseUrl/link"

		context("유효한 요청이 전달 되면") {
			val requestJson = toJson(getCoupleLinkRequest())
			val response = getCoupleLinkResponse()
			val request = request(HttpMethod.POST, url)
				.header(AUTHORIZATION, "Bearer access-token")
				.content(requestJson)
				.contentType(APPLICATION_JSON)
			it("1000 SUCCESS") {
				every { coupleCodeService.linkCouple(any()) } returns response

				mockMvc
					.perform(request)
					.andExpect(status().isOk)
					.andExpectData(
						jsonPath("$.code") shouldBe ReturnCode.SUCCESS.code,
						jsonPath("$.message") shouldBe ReturnCode.SUCCESS.message,
						jsonPath("$.data.partnerId") shouldBe response.partnerId
					)
					.andDocument(
						"1000-link-couple",
						requestHeaders(
							"Authorization" headerMeans "액세스 토큰"
						),
						requestBody(
							"coupleCode" type STRING means "커플 연동 코드"
						),
						envelopeResponseBody("data.partnerId" type NUMBER means "파트너 아이디")
					)
			}
		}

		context("애플 테스트 요청이 전달 되면") {
			val requestJson = toJson(CoupleLinkRequest("appleTestCode"))
			val response = getCoupleLinkResponse()
			val request = request(HttpMethod.POST, url)
				.header(AUTHORIZATION, "Bearer access-token")
				.content(requestJson)
				.contentType(APPLICATION_JSON)

			it("1000 SUCCESS") {
				every { coupleCodeService.linkCouple(any()) } returns response

				mockMvc
					.perform(request)
					.andExpect(status().isOk)
					.andExpectData(
						jsonPath("$.code") shouldBe ReturnCode.SUCCESS.code,
						jsonPath("$.message") shouldBe ReturnCode.SUCCESS.message,
						jsonPath("$.data.partnerId") shouldBe response.partnerId
					)
					.andDocument(
						"1000-link-apple",
						requestHeaders(
							"Authorization" headerMeans "액세스 토큰"
						),
						requestBody(
							"coupleCode" type STRING means "커플 연동 코드"
						),
						envelopeResponseBody("data.partnerId" type NUMBER means "파트너 아이디")
					)
			}
		}

		context("유효하지 않은 코드로 요청이 전달 되면") {
			val requestJson = toJson(CoupleLinkRequest("appleTestCode"))
			val request = request(HttpMethod.POST, url)
				.header(AUTHORIZATION, "Bearer access-token")
				.content(requestJson)
				.contentType(APPLICATION_JSON)

			it("8000 WRONG_PARAMETER") {
				every { coupleCodeService.linkCouple(any()) } throws LbException(ReturnCode.WRONG_PARAMETER)

				mockMvc
					.perform(request)
					.andExpect(status().isBadRequest)
					.andExpectData(
						jsonPath("$.code") shouldBe ReturnCode.WRONG_PARAMETER.code,
						jsonPath("$.message") shouldBe ReturnCode.WRONG_PARAMETER.message
					)
					.andDocument(
						"8000-wrong-code",
						requestHeaders(
							"Authorization" headerMeans "액세스 토큰"
						),
						requestBody(
							"coupleCode" type STRING means "커플 연동 코드"
						),
						envelopeResponseBody(dataOptional = true)
					)
			}
		}

		context("본인의 코드로 요청이 전달 되면") {
			val requestJson = toJson(CoupleLinkRequest("appleTestCode"))
			val request = request(HttpMethod.POST, url)
				.header(AUTHORIZATION, "Bearer access-token")
				.content(requestJson)
				.contentType(APPLICATION_JSON)

			it("1301 CAN_NOT_LINK_SELF") {
				every { coupleCodeService.linkCouple(any()) } throws LbException(ReturnCode.CAN_NOT_LINK_SELF)

				mockMvc
					.perform(request)
					.andExpect(status().isBadRequest)
					.andExpectData(
						jsonPath("$.code") shouldBe ReturnCode.CAN_NOT_LINK_SELF.code,
						jsonPath("$.message") shouldBe ReturnCode.CAN_NOT_LINK_SELF.message
					)
					.andDocument(
						"1301-self-code",
						requestHeaders(
							"Authorization" headerMeans "액세스 토큰"
						),
						requestBody(
							"coupleCode" type STRING means "커플 연동 코드"
						),
						envelopeResponseBody(dataOptional = true)
					)
			}
		}

		context("이미 연동된 유저의 요청이 전달 되면") {
			val requestJson = toJson(CoupleLinkRequest("appleTestCode"))
			val request = request(HttpMethod.POST, url)
				.header(AUTHORIZATION, "Bearer access-token")
				.content(requestJson)
				.contentType(APPLICATION_JSON)

			it("1300 ALREADY_EXIST_COUPLE") {
				every { coupleCodeService.linkCouple(any()) } throws LbException(ReturnCode.ALREADY_EXIST_COUPLE)

				mockMvc
					.perform(request)
					.andExpect(status().isBadRequest)
					.andExpectData(
						jsonPath("$.code") shouldBe ReturnCode.ALREADY_EXIST_COUPLE.code,
						jsonPath("$.message") shouldBe ReturnCode.ALREADY_EXIST_COUPLE.message
					)
					.andDocument(
						"1300-already-code",
						requestHeaders(
							"Authorization" headerMeans "액세스 토큰"
						),
						requestBody(
							"coupleCode" type STRING means "커플 연동 코드"
						),
						envelopeResponseBody(dataOptional = true)
					)
			}
		}
	}
}) {
	companion object {
		fun getCoupleCodeResponse(): CoupleCodeResponse {
			return CoupleCodeResponse(
				coupleCode = "ABCDEFGHI",
				remainSeconds = 86400
			)
		}

		fun getCoupleLinkRequest(): CoupleLinkRequest {
			return CoupleLinkRequest(
				coupleCode = "ABCDEFGHI"
			)
		}

		fun getCoupleLinkResponse(): CoupleLinkResponse {
			return CoupleLinkResponse(
				partnerId = 2L
			)
		}
	}
}
