package com.lovebird.api.controller.external

import com.lovebird.api.common.base.ControllerDescribeSpec
import com.lovebird.api.dto.request.external.DiaryUploadPresignedUrlRequest
import com.lovebird.api.dto.request.external.ProfileUploadPresignedUrlRequest
import com.lovebird.api.dto.response.external.PresignedUrlListResponse
import com.lovebird.api.dto.response.external.PresignedUrlResponse
import com.lovebird.api.service.external.PresignedUrlService
import com.lovebird.api.utils.andExpectData
import com.lovebird.api.utils.restdocs.ARRAY
import com.lovebird.api.utils.restdocs.NUMBER
import com.lovebird.api.utils.restdocs.OBJECT
import com.lovebird.api.utils.restdocs.STRING
import com.lovebird.api.utils.restdocs.andDocument
import com.lovebird.api.utils.restdocs.headerMeans
import com.lovebird.api.utils.restdocs.requestBody
import com.lovebird.api.utils.restdocs.requestHeaders
import com.lovebird.api.utils.restdocs.responseBody
import com.lovebird.api.utils.restdocs.restDocMockMvcBuild
import com.lovebird.api.utils.restdocs.type
import com.lovebird.api.utils.shouldBe
import com.lovebird.common.enums.ReturnCode
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

@WebMvcTest(PresignedUrlController::class)
class PresignedUrlControllerTest(
	@MockkBean(relaxed = true)
	private val presignedUrlService: PresignedUrlService,
	@Autowired
	private val context: WebApplicationContext
) : ControllerDescribeSpec({

	val baseUrl = "/api/v1/presigned-urls"
	val restDocumentation = ManualRestDocumentation()
	val mockMvc = restDocMockMvcBuild(context, restDocumentation)

	beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
	afterEach { restDocumentation.afterTest() }

	describe("POST : /api/v1/presigned-urls/profile") {
		val url = "$baseUrl/profile"

		context("유효한 요청이 전달 되면") {
			val requestBody = ProfileUploadPresignedUrlRequest("test.png")
			val requestJson = toJson(requestBody)
			val response = PresignedUrlResponse("https://cdn.lovebird.com/profile/1-profile.png", "1-profile.png")
			val request = request(HttpMethod.POST, url)
				.header(AUTHORIZATION, "Bearer access-token")
				.contentType(APPLICATION_JSON)
				.content(requestJson)

			it("1000 SUCCESS") {
				every { presignedUrlService.getProfilePresignedUrl(requestBody.toParam(1L)) } returns response

				mockMvc
					.perform(request)
					.andExpect(status().isOk)
					.andExpectData(
						jsonPath("$.code") shouldBe ReturnCode.SUCCESS.code,
						jsonPath("$.message") shouldBe ReturnCode.SUCCESS.message,
						jsonPath("$.data.presignedUrl") shouldBe response.presignedUrl,
						jsonPath("$.data.filename") shouldBe response.filename
					)
					.andDocument(
						"1000-profile-presigned-url",
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

	describe("POST : /api/v1/presigned-urls/diary") {
		val url = "$baseUrl/diary"
		val filenames = listOf("test1.png", "test2.png")
		val presignedUrls: List<PresignedUrlResponse> = listOf(
			PresignedUrlResponse("https://cdn.lovebird.com/profile/1_1-1.png", "1_1-1.png"),
			PresignedUrlResponse("https://cdn.lovebird.com/profile/1_1-2.png", "1_1-2.png")
		)

		context("유효한 요청이 전달 되면") {
			val requestBody = DiaryUploadPresignedUrlRequest(filenames, 1L)
			val requestJson = toJson(requestBody)
			val response = PresignedUrlListResponse.of(presignedUrls)
			val request = request(HttpMethod.POST, url)
				.header(AUTHORIZATION, "Bearer access-token")
				.contentType(APPLICATION_JSON)
				.content(requestJson)

			it("1000 OK") {
				every { presignedUrlService.getDiaryPresignedUrls(requestBody.toParam(1L)) } returns response

				mockMvc
					.perform(request)
					.andExpect(status().isOk)
					.andExpectData(
						jsonPath("$.code") shouldBe ReturnCode.SUCCESS.code,
						jsonPath("$.message") shouldBe ReturnCode.SUCCESS.message,
						jsonPath("$.data.presignedUrls[0].presignedUrl") shouldBe response.presignedUrls[0].presignedUrl,
						jsonPath("$.data.presignedUrls[0].filename") shouldBe response.presignedUrls[0].filename,
						jsonPath("$.data.presignedUrls[1].presignedUrl") shouldBe response.presignedUrls[1].presignedUrl,
						jsonPath("$.data.presignedUrls[1].filename") shouldBe response.presignedUrls[1].filename,
						jsonPath("$.data.totalCount") shouldBe response.totalCount
					)
					.andDocument(
						"1000-diary-presigned-url",
						requestHeaders(
							"Authorization" headerMeans "액세스 토큰"
						),
						requestBody(
							"filenames" type ARRAY means "파일 이름 리스트" isOptional false,
							"diaryId" type NUMBER means "다이어리 ID" isOptional false
						),
						responseBody(
							"code" type STRING means "응답 코드",
							"message" type STRING means "응답 메시지",
							"data" type OBJECT means "응답 데이터",
							"data.presignedUrls" type ARRAY means "Presigned Url 리스트",
							"data.presignedUrls[].presignedUrl" type STRING means "Presigned Url",
							"data.presignedUrls[].filename" type STRING means "파일 이름",
							"data.totalCount" type NUMBER means "총 개수"
						)
					)
			}
		}
	}
})
