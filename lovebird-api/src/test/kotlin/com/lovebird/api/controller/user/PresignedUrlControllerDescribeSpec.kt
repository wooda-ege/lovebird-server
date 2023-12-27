package com.lovebird.api.controller.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.lovebird.api.config.WebMvcConfig
import com.lovebird.api.controller.external.PresignedUrlController
import com.lovebird.api.dto.request.external.ProfileUploadPresignedUrlRequest
import com.lovebird.api.dto.response.external.PresignedUrlResponse
import com.lovebird.api.service.external.PresignedUrlService
import com.lovebird.api.utils.restdocs.OBJECT
import com.lovebird.api.utils.restdocs.STRING
import com.lovebird.api.utils.restdocs.andDocument
import com.lovebird.api.utils.restdocs.headerMeans
import com.lovebird.api.utils.restdocs.requestBody
import com.lovebird.api.utils.restdocs.requestHeaders
import com.lovebird.api.utils.restdocs.responseBody
import com.lovebird.api.utils.restdocs.restDocMockMvcBuild
import com.lovebird.api.utils.restdocs.type
import com.lovebird.common.enums.ReturnCode
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.request
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext

@Import(WebMvcConfig::class)
@ExtendWith(RestDocumentationExtension::class)
@WebMvcTest(PresignedUrlController::class)
class PresignedUrlControllerDescribeSpec(
	@MockkBean(relaxed = true)
	private val presignedUrlService: PresignedUrlService,
	@Autowired
	private val context: WebApplicationContext
) : DescribeSpec({

	val baseUrl = "/api/v1/presigned-urls"
	val restDocumentation = ManualRestDocumentation()
	val mockMvc = restDocMockMvcBuild(context, restDocumentation)

	beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
	afterEach { restDocumentation.afterTest() }

	describe("POST : /api/v1/presigned-urls") {
		val url = "$baseUrl/profile"

		context("유효한 요청이 전달 되면") {
			val requestBody = ProfileUploadPresignedUrlRequest("test.png")
			val requestJson = toJson(requestBody)
			val response = PresignedUrlResponse("https://cdn.lovebird.com/profile/~", "1-1.png")
			val request = request(HttpMethod.POST, url)
				.header(AUTHORIZATION, "Bearer access-token")
				.contentType(APPLICATION_JSON)
				.content(requestJson)

			every { presignedUrlService.getProfilePresignedUrl(requestBody.toParam(1L)) } returns response

			it("200") {
				mockMvc
					.perform(request)
					.andExpect(status().isOk)
					.andExpect(jsonPath("$.code").value(ReturnCode.SUCCESS.code))
					.andExpect(jsonPath("$.message").value(ReturnCode.SUCCESS.message))
					.andExpect(jsonPath("$.data.presignedUrl").value(response.presignedUrl))
					.andExpect(jsonPath("$.data.filename").value(response.filename))
					.andDocument(
						"profile-presigned-url",
						requestHeaders(
							"Authorization" headerMeans "액세스 토큰"
						),
						requestBody(
							"filename" type STRING means "파일 이름" isOptional false
						),
						responseBody(
							"code" type STRING means "응답 코드",
							"message" type STRING means "응답 메시지",
							"data" type OBJECT means "응답 데이터",
							"data.presignedUrl" type STRING means "업로드용 Presigned Url",
							"data.filename" type STRING means "파일 새 이름"
						)
					)
			}
		}
	}
}) {

	companion object {
		private val mapper: ObjectMapper = ObjectMapper()

		fun toJson(value: Any): String {
			return mapper.writeValueAsString(value)
		}
	}
}
