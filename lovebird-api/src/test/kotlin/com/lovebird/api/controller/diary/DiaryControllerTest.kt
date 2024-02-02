package com.lovebird.api.controller.diary

import com.lovebird.api.common.base.ControllerDescribeSpec
import com.lovebird.api.dto.response.diary.DiaryListResponse
import com.lovebird.api.dto.response.diary.DiarySimpleListResponse
import com.lovebird.api.service.diary.DiaryService
import com.lovebird.api.utils.CommonTestFixture
import com.lovebird.api.utils.DiaryTestFixture
import com.lovebird.api.utils.restdocs.ARRAY
import com.lovebird.api.utils.restdocs.DATE
import com.lovebird.api.utils.restdocs.NUMBER
import com.lovebird.api.utils.restdocs.RestDocsField
import com.lovebird.api.utils.restdocs.STRING
import com.lovebird.api.utils.restdocs.andDocument
import com.lovebird.api.utils.restdocs.headerMeans
import com.lovebird.api.utils.restdocs.pathMeans
import com.lovebird.api.utils.restdocs.queryParameters
import com.lovebird.api.utils.restdocs.requestHeaders
import com.lovebird.api.utils.restdocs.restDocMockMvcBuild
import com.lovebird.api.utils.restdocs.type
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.request
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@WebMvcTest(DiaryController::class)
class DiaryControllerTest(
	@MockkBean(relaxed = true)
	private val diaryService: DiaryService,
	@Autowired
	private val context: WebApplicationContext
) : ControllerDescribeSpec({

	val baseUrl = "/api/v1/diaries"
	val restDocumentation = ManualRestDocumentation()
	val mockMvc = restDocMockMvcBuild(context, restDocumentation)
	val localDate = LocalDate.now()
	val memoryDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
	beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
	afterEach { restDocumentation.afterTest() }

	describe("GET : /api/v1/diaries/memory-date?memoryDate=$memoryDate") {
		val url = "$baseUrl/memory-date?memoryDate=$memoryDate"

		context("memory-date 기준으로 다이어리 목록을 요청하면") {
			val request = request(HttpMethod.GET, url)
				.header(HttpHeaders.AUTHORIZATION, "Bearer access-token")

			val user = CommonTestFixture.getUser(1L)
			val diaries = DiaryTestFixture.getDiarySimpleResponseList(user, null, 5)
			val response = DiarySimpleListResponse.of(diaries)

			it("1000 SUCCESS") {
				every { diaryService.findAllByMemoryDate(any(), any()) } returns response

				mockMvc
					.perform(request)
					.andExpect(status().isOk)
					.andDocument(
						"1000-diary-list-memory-date",
						requestHeaders(
							"Authorization" headerMeans "액세스 토큰"
						),
						queryParameters(
							"memoryDate" pathMeans "데이트 날짜"
						),
						envelopeResponseBody(
							"data.diaries" type ARRAY means "다이어리 목록",
							"data.totalCount" type NUMBER means "캘린더 개수"
						)
							.andWithPrefix("data.diaries[].", getSimpleDiaryDetailResponseSnippet())
					)
			}
		}
	}

	describe("GET : /api/v1/diaries/cursor?memoryDate=$memoryDate&searchType=BEFORE&pageSize=10") {
		val url = "$baseUrl/cursor?memoryDate=$memoryDate&searchType=BEFORE&diaryId=&pageSize=10"

		context("커서 기반으로 다이어리 목록을 요청하면") {
			val request = request(HttpMethod.GET, url)
				.header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
			val diaries = DiaryTestFixture.getDiaries(10)
			val response = DiaryListResponse.of(diaries)

			it("1000 SUCCESS") {
				every { diaryService.findPageByCursor(any(), any()) } returns response

				mockMvc
					.perform(request)
					.andExpect(status().isOk)
					.andDocument(
						"1000-diary-list-cursor",
						requestHeaders(
							"Authorization" headerMeans "액세스 토큰"
						),
						queryParameters(
							"memoryDate" pathMeans "데이트 날짜",
							"searchType" pathMeans "커서 기준 BEFORE 또는 AFTER",
							"diaryId" pathMeans "커서(다이어리) 아이디 -> 초기 요청 시 값이 없어도 됨 단, KEY 값은 존재해야함",
							"pageSize" pathMeans "요청할 다이어리 목록 최대 크기"
						),
						envelopeResponseBody(
							"data.diaries" type ARRAY means "다이어리 목록",
							"data.totalCount" type NUMBER means "캘린더 개수",
							"data.diaryId" type NUMBER means "커서(다이어리 아이디) 아이디",
							"data.memoryDate" type DATE means "커서(다이어리 아이디) 기준 데이트 날짜"
						)
							.andWithPrefix("data.diaries[].", getDiaryDetailResponseSnippet())
					)
			}
		}
	}

	describe("GET : /api/v1/diaries/{diaryId}") {
	}

	describe("POST : /api/v1/diaries") {
	}

	describe("PUT : /api/v1/diaries/{diaryId}") {
	}

	describe("DELETE : /api/v1/diaries/{diaryId}") {
	}
}) {
	companion object {
		fun getSimpleDiaryDetailResponseSnippet(): List<FieldDescriptor> {
			return responseDiaryDetailResponse(
				"diaryId" type NUMBER means "다이어리 아이디",
				"userId" type NUMBER means "유저 아이디",
				"title" type STRING means "제목",
				"memoryDate" type DATE means "데이트 날짜",
				"place" type STRING means "장소",
				"content" type STRING means "내용",
				"imageUrl" type STRING means "이미지 URL"
			)
		}

		fun getDiaryDetailResponseSnippet(): List<FieldDescriptor> {
			return responseDiaryDetailResponse(
				"diaryId" type NUMBER means "다이어리 아이디",
				"userId" type NUMBER means "유저 아이디",
				"title" type STRING means "제목",
				"memoryDate" type DATE means "데이트 날짜",
				"place" type STRING means "장소",
				"content" type STRING means "내용",
				"imageUrls" type ARRAY means "이미지 URL 목록"
			)
		}

		private fun responseDiaryDetailResponse(vararg fields: RestDocsField): List<FieldDescriptor> {
			return fields.map { it.descriptor }
		}
	}
}
