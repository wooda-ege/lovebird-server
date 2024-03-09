package com.lovebird.api.controller.calendar

import com.lovebird.api.common.base.ControllerDescribeSpec
import com.lovebird.api.common.base.ServiceDescribeSpec
import com.lovebird.api.dto.request.calendar.CalendarCreateRequest
import com.lovebird.api.dto.request.calendar.CalendarListRequest
import com.lovebird.api.dto.request.calendar.CalendarUpdateRequest
import com.lovebird.api.dto.response.calendar.CalendarDetailResponse
import com.lovebird.api.dto.response.calendar.CalendarListResponse
import com.lovebird.api.service.calendar.CalendarService
import com.lovebird.api.service.calendar.CalendarServiceTest
import com.lovebird.api.utils.restdocs.ARRAY
import com.lovebird.api.utils.restdocs.DATE
import com.lovebird.api.utils.restdocs.DATETIME
import com.lovebird.api.utils.restdocs.NUMBER
import com.lovebird.api.utils.restdocs.RestDocsField
import com.lovebird.api.utils.restdocs.STRING
import com.lovebird.api.utils.restdocs.andDocument
import com.lovebird.api.utils.restdocs.headerMeans
import com.lovebird.api.utils.restdocs.requestBody
import com.lovebird.api.utils.restdocs.requestHeaders
import com.lovebird.api.utils.restdocs.restDocMockMvcBuild
import com.lovebird.api.utils.restdocs.type
import com.lovebird.common.enums.Alarm
import com.lovebird.common.enums.Color
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.request
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext
import java.time.LocalDate

@WebMvcTest(CalendarController::class)
class CalendarControllerTest(
	@MockkBean(relaxed = true)
	private val calendarService: CalendarService,
	@Autowired
	private val context: WebApplicationContext
) : ControllerDescribeSpec({

	val baseUrl = "/api/v1/calendars"
	val restDocumentation = ManualRestDocumentation()
	val mockMvc = restDocMockMvcBuild(context, restDocumentation)

	beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
	afterEach { restDocumentation.afterTest() }

	describe("GET : /api/v1/calendars/{id}") {
		val url = "$baseUrl/1"
		context("캘린더 id를 명시하면") {
			val request = request(HttpMethod.GET, url)
				.header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
				.contentType(MediaType.APPLICATION_JSON)

			it("1000 SUCCESS") {
				every { calendarService.findById(any()) } returns getCalendarDetailResponse()

				mockMvc.perform(request)
					.andExpect(status().isOk)
					.andDocument(
						"1000-calendar",
						requestHeaders(
							"Authorization" headerMeans "액세스 토큰"
						),
						envelopeResponseBody()
							.andWithPrefix("data.", getCalendarDetailResponseSnippet())
					)
			}
		}
	}

	describe("GET: /api/v1/calendars") {
		val url = "$baseUrl?year=2024&month=1"
		context("year 또는 month 기준으로 캘린더 목록을 요청하면") {
			val requestBody = getCalendarListRequest()
			val request = request(HttpMethod.GET, url)
				.header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
				.contentType(MediaType.APPLICATION_JSON)

			val user = ServiceDescribeSpec.getUser(1L)
			val calendarList = CalendarServiceTest.getCalendarList(requestBody.toParam(user), null)
			val response = CalendarListResponse.of(calendarList)

			it("1000 SUCCESS") {
				every { calendarService.findCalendarsByMonthAndUser(any()) } returns response

				mockMvc
					.perform(request)
					.andExpect(status().isOk)
					.andDocument(
						"1000-calendar-list",
						requestHeaders(
							"Authorization" headerMeans "액세스 토큰"
						),
						envelopeResponseBody(
							"data.calendars" type ARRAY means "캘린더 목록",
							"data.totalCount" type NUMBER means "캘린더 개수"
						)
							.andWithPrefix("data.calendars[].", getCalendarDetailResponseSnippet())
					)
			}
		}
	}

	describe("POST: /api/v1/calendars") {
		context("유효한 요청이 전달 되면") {
			val requestBody = getCalendarCreateRequest()
			val requestJson = toJson(requestBody)
			val request = request(HttpMethod.POST, baseUrl)
				.header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson)

			it("1000 SUCCESS") {
				every { calendarService.save(any(), any()) } returns Unit

				mockMvc.perform(request)
					.andExpect(status().isCreated)
					.andDocument(
						"1000-calendar-post",
						requestHeaders(
							"Authorization" headerMeans "액세스 토큰"
						),
						requestBody(
							"title" type STRING means "제목",
							"memo" type STRING means "메모" isOptional true,
							"color" type STRING means "색깔",
							"alarm" type STRING means "알람 종류" isOptional true,
							"startDate" type DATE means "일정 시작일",
							"endDate" type DATE means "일정 종료일" isOptional true,
							"startTime" type DATETIME means "일정 시작시간" isOptional true,
							"endTime" type DATETIME means "일정 종료 시간" isOptional true
						),
						envelopeResponseBody(dataOptional = true)
					)
			}
		}
	}

	describe("PUT : /api/v1/calendars/{id}") {
		val url = "$baseUrl/1"
		context("유효한 요청이 전달되면") {
			val requestBody = getCalendarUpdateRequest()
			val requestJson = toJson(requestBody)
			val request = request(HttpMethod.PUT, url)
				.header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson)

			it("1000 SUCCESS") {
				every { calendarService.update(any()) } returns Unit

				mockMvc.perform(request)
					.andExpect(status().isOk)
					.andDocument(
						"1000-calendar-put",
						requestHeaders(
							"Authorization" headerMeans "액세스 토큰"
						),
						requestBody(
							"title" type STRING means "제목",
							"memo" type STRING means "메모" isOptional true,
							"color" type STRING means "색깔",
							"alarm" type STRING means "알람 종류",
							"startDate" type DATE means "일정 시작일",
							"endDate" type DATE means "일정 종료일",
							"startTime" type DATETIME means "일정 시작시간" isOptional true,
							"endTime" type DATETIME means "일정 종료 시간" isOptional true
						),
						envelopeResponseBody()
					)
			}
		}
	}

	describe("DELETE : /api/v1/calendars/{id}") {
		val url = "$baseUrl/1"
		context("유효한 요청이 전달되면") {
			val request = request(HttpMethod.DELETE, url)
				.header(HttpHeaders.AUTHORIZATION, "Bearer access-token")

			it("1000 SUCCESS") {
				every { calendarService.delete(any(), any()) } returns Unit

				mockMvc.perform(request)
					.andExpect(status().isOk)
					.andDocument(
						"1000-calendar-delete",
						requestHeaders(
							"Authorization" headerMeans "액세스 토큰"
						),
						envelopeResponseBody()
					)
			}
		}
	}
}) {
	companion object {
		fun getCalendarDetailResponse(): CalendarDetailResponse {
			return CalendarDetailResponse(
				id = 1L,
				userId = 1L,
				title = "데이트 약속",
				memo = "까먹지 말기!!",
				color = Color.PRIMARY.name,
				alarm = Alarm.NONE.name,
				startDate = LocalDate.of(2023, 12, 24),
				endDate = LocalDate.of(2023, 12, 25),
				startTime = null,
				endTime = null
			)
		}

		private fun getCalendarDetailResponseSnippet(): List<FieldDescriptor> {
			return responseCalendarDetailResponse(
				"id" type NUMBER means "캘린더 아이디",
				"userId" type NUMBER means "유저 아이디",
				"title" type STRING means "제목",
				"memo" type STRING means "메모" isOptional true,
				"color" type STRING means "색깔",
				"alarm" type STRING means "알람 종류",
				"startDate" type DATE means "일정 시작일",
				"endDate" type DATE means "일정 종료일",
				"startTime" type DATETIME means "일정 시작시간" isOptional true,
				"endTime" type DATETIME means "일정 종료 시간" isOptional true
			)
		}

		private fun getCalendarListRequest(): CalendarListRequest {
			return CalendarListRequest(
				year = 2023,
				month = 12
			)
		}

		private fun getCalendarCreateRequest(): CalendarCreateRequest {
			return CalendarCreateRequest(
				title = "캘린더 제목",
				memo = "중요함",
				color = Color.PRIMARY,
				alarm = Alarm.NONE,
				startDate = LocalDate.of(2023, 12, 24),
				endDate = null,
				startTime = null,
				endTime = null
			)
		}

		private fun getCalendarUpdateRequest(): CalendarUpdateRequest {
			return CalendarUpdateRequest(
				title = "캘린더 제목",
				memo = "중요함",
				color = Color.PRIMARY,
				alarm = Alarm.NONE,
				startDate = LocalDate.of(2023, 12, 24),
				endDate = LocalDate.of(2023, 12, 25),
				startTime = null,
				endTime = null
			)
		}

		private fun responseCalendarDetailResponse(vararg fields: RestDocsField): List<FieldDescriptor> {
			return fields.map { it.descriptor }
		}
	}
}
