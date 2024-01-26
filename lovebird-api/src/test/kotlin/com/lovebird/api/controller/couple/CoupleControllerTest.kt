package com.lovebird.api.controller.couple

import com.lovebird.api.common.base.ControllerDescribeSpec
import com.lovebird.api.dto.response.couple.CoupleCheckResponse
import com.lovebird.api.service.couple.CoupleService
import com.lovebird.api.utils.andExpectData
import com.lovebird.api.utils.restdocs.BOOLEAN
import com.lovebird.api.utils.restdocs.andDocument
import com.lovebird.api.utils.restdocs.headerMeans
import com.lovebird.api.utils.restdocs.requestHeaders
import com.lovebird.api.utils.restdocs.restDocMockMvcBuild
import com.lovebird.api.utils.restdocs.type
import com.lovebird.api.utils.shouldBe
import com.lovebird.common.enums.ReturnCode
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext

@WebMvcTest(CoupleController::class)
class CoupleControllerTest(
	@MockkBean(relaxed = true)
	private val coupleService: CoupleService,
	@Autowired
	private val context: WebApplicationContext
) : ControllerDescribeSpec({

	val baseUrl = "/api/v1/couple"
	val restDocumentation = ManualRestDocumentation()
	val mockMvc = restDocMockMvcBuild(context, restDocumentation)

	beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
	afterEach { restDocumentation.afterTest() }

	describe("GET : /api/v1/couple/check") {
		context("유효한 요청이 전달 되면") {
			val response = CoupleCheckResponse(true)
			val request = RestDocumentationRequestBuilders.request(HttpMethod.GET, "$baseUrl/check")
				.header(HttpHeaders.AUTHORIZATION, "Bearer access-token")

			it("1000 SUCCESS") {
				every { coupleService.existByUser(any()) } returns true

				mockMvc
					.perform(request)
					.andExpect(status().isOk)
					.andExpectData(
						jsonPath("$.code") shouldBe ReturnCode.SUCCESS.code,
						jsonPath("$.message") shouldBe ReturnCode.SUCCESS.message,
						jsonPath("$.data.linkedFlag") shouldBe response.linkedFlag
					)
					.andDocument(
						"1000-couple-check-url",
						requestHeaders(
							"Authorization" headerMeans "액세스 토큰"
						),
						envelopeResponseBody("data.linkedFlag" type BOOLEAN means "연동 여부")
					)
			}
		}
	}
})
