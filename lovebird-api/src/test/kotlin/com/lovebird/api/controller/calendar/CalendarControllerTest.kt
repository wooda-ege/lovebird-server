package com.lovebird.api.controller.calendar

import com.lovebird.api.common.base.ControllerDescribeSpec
import com.lovebird.api.dto.response.calendar.CalendarDetailResponse
import com.lovebird.api.service.calendar.CalendarService
import com.lovebird.api.utils.restdocs.DATE
import com.lovebird.api.utils.restdocs.DATETIME
import com.lovebird.api.utils.restdocs.NUMBER
import com.lovebird.api.utils.restdocs.RestDocsField
import com.lovebird.api.utils.restdocs.STRING
import com.lovebird.api.utils.restdocs.andDocument
import com.lovebird.api.utils.restdocs.restDocMockMvcBuild
import com.lovebird.api.utils.restdocs.type
import com.lovebird.common.enums.Alarm
import com.lovebird.common.enums.Color
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpMethod
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.web.context.WebApplicationContext
import java.time.LocalDate

@WebMvcTest(CalendarController::class)
class CalendarControllerTest(
	@MockkBean(relaxed = true)
	private val calendarService: CalendarService,
	@Autowired
	private val context: WebApplicationContext
): ControllerDescribeSpec({

	val baseUrl = "/api/v1/calendars"
	val restDocumentation = ManualRestDocumentation()
	val mockMvc = restDocMockMvcBuild(context, restDocumentation)

	beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
	afterEach { restDocumentation.afterTest() }

	describe("GET : /api/v1/calendars/{id}") {
		val url = "$baseUrl/1"
		context("캘린더 id를 명시하면") {
			val request = request(HttpMethod.GET, url)

			it("1000 SUCCESS") {
				every { calendarService.findById(any()) } returns getCalendarDetailResponse()

				mockMvc.perform(request)
					.andExpect(status().isOk)
					.andDocument(
						"1000-calendar",
						successResponseBody()
							.and(getCalendarDetailResponseSnippet())
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
				"data.id" type NUMBER means "캘린더 아이디",
				"data.userId" type NUMBER means "유저 아이디",
				"data.title" type STRING means "제목",
				"data.memo" type STRING means "메모" isOptional true,
				"data.color" type STRING means "색깔 상태",
				"data.alarm" type STRING means "알람 상태",
				"data.startDate" type DATE means "시작 날짜",
				"data.endDate" type DATE means "종료 날짜",
				"data.startTime" type DATETIME means "시작 시간" isOptional true,
				"data.endTime" type DATETIME means "종료 시간" isOptional true
			)
		}

		private fun responseCalendarDetailResponse(vararg fields: RestDocsField): List<FieldDescriptor> {
			return fields.map { it.descriptor }
		}
	}
}
