package com.lovebird.api.controller.calendar

import com.lovebird.api.common.base.ControllerDescribeSpec
import com.lovebird.api.common.base.ServiceDescribeSpec
import com.lovebird.api.dto.request.calendar.CalendarListRequest
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
						successResponseBody()
							.andWithPrefix("data.", getCalendarDetailResponseSnippet())
					)
			}
		}
	}

	describe("GET: /api/v1/calendars") {
		context("year 또는 month 기준으로 캘린더 목록을 요청하면") {
			val requestBody = getCalendarListRequest()
			val requestJson = toJson(requestBody)
			val request = request(HttpMethod.GET, baseUrl)
				.header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson)
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
						requestBody(
							"year" type NUMBER means "년도(Year)",
							"month" type NUMBER means "월(Month)"
						),
						successResponseBody(
							"data.calendars" type ARRAY means "캘린더 목록",
							"data.totalCount" type NUMBER means "캘린더 개수"
						)
							.andWithPrefix("data.calendars[].", getCalendarDetailResponseSnippet())
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
				"color" type STRING means "색깔 상태",
				"alarm" type STRING means "알람 상태",
				"startDate" type DATE means "시작 날짜",
				"endDate" type DATE means "종료 날짜",
				"startTime" type DATETIME means "시작 시간" isOptional true,
				"endTime" type DATETIME means "종료 시간" isOptional true
			)
		}

		fun getCalendarListRequest(): CalendarListRequest {
			return CalendarListRequest(
				year = 2023,
				month = 12
			)
		}

		private fun responseCalendarDetailResponse(vararg fields: RestDocsField): List<FieldDescriptor> {
			return fields.map { it.descriptor }
		}
	}
}
