package com.lovebird.api.service.calendar

import com.lovebird.api.common.base.ServiceDescribeSpec
import com.lovebird.api.dto.param.calendar.CalendarListParam
import com.lovebird.api.dto.request.calendar.CalendarCreateRequest
import com.lovebird.api.dto.request.calendar.CalendarUpdateRequest
import com.lovebird.api.dto.response.calendar.CalendarDetailResponse
import com.lovebird.api.dto.response.calendar.CalendarListResponse
import com.lovebird.common.enums.Alarm
import com.lovebird.common.enums.Color
import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.lovebird.domain.dto.query.CalendarListResponseParam
import com.lovebird.domain.entity.Calendar
import com.lovebird.domain.entity.CoupleEntry
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.reader.CalendarEventReader
import com.lovebird.domain.repository.reader.CalendarReader
import com.lovebird.domain.repository.reader.CoupleEntryReader
import com.lovebird.domain.repository.writer.CalendarEventWriter
import com.lovebird.domain.repository.writer.CalendarWriter
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.startWith
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.test.util.ReflectionTestUtils
import java.time.LocalDate

class CalendarServiceTest : ServiceDescribeSpec({
	val calendarReader = mockk<CalendarReader>(relaxed = true)
	val coupleEntryReader = mockk<CoupleEntryReader>(relaxed = true)
	val calendarWriter = mockk<CalendarWriter>(relaxed = true)
	val calendarEventReader = mockk<CalendarEventReader>(relaxed = true)
	val calendarEventWriter = mockk<CalendarEventWriter>(relaxed = true)
	val calendarService = CalendarService(
		calendarReader = calendarReader,
		coupleEntryReader = coupleEntryReader,
		calendarWriter = calendarWriter,
		calendarEventWriter = calendarEventWriter,
		calendarEventReader = calendarEventReader
	)

	afterEach {
		clearMocks(calendarReader, coupleEntryReader, calendarWriter, calendarEventReader, calendarEventWriter)
	}


	describe("캘린더를 ID로 찾는 로직") {
		context("존재하는 id가 주어졌다면") {
			every { calendarReader.findEntityById(1L) } returns getCalendar(1L, 1L)
			val calendarDetailResponse: CalendarDetailResponse = getCalendarDetailResponse(getCalendar(1L, 1L))

			it("해당 id에 대한 캘린더 정보가 반한된다") {
				calendarService.findById(1L) shouldBe calendarDetailResponse
			}
		}

		context("존재하지 않는 id가 주어졌다면") {
			every { calendarReader.findEntityById(any()) } throws LbException(ReturnCode.WRONG_PARAMETER)

			it("예외가 발생한다") {
				val exception = shouldThrow<LbException> {
					calendarService.findById(1)
				}

				exception.getMsg() should startWith(ReturnCode.WRONG_PARAMETER.message)
				exception.getCode() should startWith(ReturnCode.WRONG_PARAMETER.code)
			}
		}
	}

	describe("캘린더를 유저정보로 검색할때") {
		context("존재하는 유저 정보가 주어졌을때") {
			val param = CalendarListParam(2023, 12, getUser(1L))
			val user: User = param.user

			it("파트너가 존재한다면 캘린더 조회에 성공한다") {
				val partnerId: Long = 2
				val calendarListResponseParam = getCalendarList(param, partnerId)
				val coupleEntry = getCoupleEntry(user.id!!, partnerId)
				every {
					coupleEntryReader.findByUser(param.user)
				} returns coupleEntry
				every {
					calendarReader.findCalendarsByDate(param.toRequestParam(coupleEntry.partner))
				} returns calendarListResponseParam

				calendarService.findCalendarsByMonthAndUser(param) should {
					CalendarListResponse(calendarListResponseParam)
				}
			}

			it("파트너가 존재하지 않아도 캘린더 조회에 성공한다") {
				val calendarListResponseParam = getCalendarList(param, null)
				every {
					coupleEntryReader.findByUser(param.user)
				} returns null
				every {
					calendarReader.findCalendarsByDate(param.toRequestParam())
				} returns calendarListResponseParam

				calendarService.findCalendarsByMonthAndUser(param) should {
					CalendarListResponse(calendarListResponseParam)
				}
			}
		}
	}

	describe("캘린더를 저장할때") {
		val user = getUser(1L)
		val request = getCalendarCreateRequest()

		context("캘린더 정보가 주어졌을 때") {
			it("파트너가 없다면 캘린더 저장과 작성자 이벤트만 저장한다") {
				every { coupleEntryReader.findByUser(user) } returns null

				calendarService.save(request, user)

				//행위 검증
				verify(exactly = 1) {
					calendarWriter.save(any())
					coupleEntryReader.findByUser(user)
					calendarEventWriter.save(any())
				}
			}

			it("파트너가 존재한다면 캘린더 저장과 작성자와 파트너의 이벤트를 모두 저장한다") {
				every { coupleEntryReader.findByUser(user) } returns getCoupleEntry(user.id!!, 2L)

				calendarService.save(request, user)

				//행위 검증
				verify(exactly = 1) {
					calendarWriter.save(any())
					coupleEntryReader.findByUser(user)
				}
				verify(exactly = 2) {
					calendarEventWriter.save(any())
				}
			}
		}
	}

	describe("캘린더를 업데이트 할 때") {
		val request = getCalendarUpdateRequest()
		val user = getUser(1L)

		context("캘린더 작성자와 업데이트 요청한 유저가 동일하다면") {
			val calendar = getCalendar(1L, user.id!!)
			every { calendarReader.findEntityById(calendar.id!!) } returns calendar

			it("캘린더 업데이트에 성공한다") {
				calendarService.update(request.toParam(calendar.id!!, user))

				//행위 검증
				verify(exactly = 1) {
					calendarReader.findEntityById(calendar.id!!)
					calendarEventReader.findCalendarEventsByCalendar(calendar)
				}
			}
		}

		context("캘린더 작성자와 업데이트 요청한 유저가 동일하지 않다면") {
			val calendar = getCalendar(1L, 2L)
			every { calendarReader.findEntityById(calendar.id!!) } returns calendar

			it("예외가 발생한다") {
				val exception = shouldThrow<LbException> {
					calendarService.update(request.toParam(calendar.id!!, user))
				}

				//예외 상태 검증
				exception.getMsg() should startWith(ReturnCode.INVALID_MEMBER.message)
				exception.getCode() should startWith(ReturnCode.INVALID_MEMBER.code)

				//행위 검증
				verify(exactly = 1) {
					calendarReader.findEntityById(calendar.id!!)
				}
			}
		}
	}

	describe("캘린더를 삭제할 때") {
		val user = getUser(1L)

		context("캘린더 작성자와 유저 정보가 같다면") {
			val calendar = getCalendar(1L, user.id!!)
			every { calendarReader.findEntityById(calendar.id!!) } returns calendar

			it("삭제에 성공한다") {
				calendarService.delete(calendar.id!!, user)

				//행위 검증
				verify(exactly = 1) {
					calendarReader.findEntityById(calendar.id!!)
					calendarEventReader.findCalendarEventsByCalendar(calendar)
					calendarEventWriter.deleteAll(any())
					calendarWriter.delete(calendar)
				}
			}
		}

		context("캘린더 작성자와 유저 정보가 같지 않다면") {
			val calendar = getCalendar(1L, 2L)
			every { calendarReader.findEntityById(calendar.id!!) } returns calendar

			it("예외가 발생한다") {
				val exception = shouldThrow<LbException> {
					calendarService.delete(calendar.id!!, user)
				}

				//예외 상태 검증
				exception.getMsg() should startWith(ReturnCode.INVALID_MEMBER.message)
				exception.getCode() should startWith(ReturnCode.INVALID_MEMBER.code)

				//행위 검증
				verify(exactly = 1) {
					calendarReader.findEntityById(calendar.id!!)
				}
			}
		}
	}
}) {
	companion object {
		fun getCalendar(calendarId: Long, userId: Long): Calendar {
			val calendar = Calendar(
				title = "테스트 제목",
				memo = "테스트 메모",
				startDate = LocalDate.of(2024, 1, 1),
				endDate = LocalDate.of(2024, 1, 2),
				startTime = null,
				endTime = null,
				color = null,
				user = getUser(userId),
				alarm = null
			)

			ReflectionTestUtils.setField(calendar, "id", calendarId)

			return calendar
		}

		fun getCalendarDetailResponse(calendar: Calendar): CalendarDetailResponse {
			return CalendarDetailResponse.of(calendar)
		}

		fun getCoupleEntry(userId: Long, partnerId: Long): CoupleEntry {
			return CoupleEntry(
				user = getUser(userId),
				partner = getUser(partnerId)
			)
		}

		fun getCalendarList(calendarListParam: CalendarListParam, partnerId: Long?): List<CalendarListResponseParam> {
			val calendarListResponseParam = arrayListOf<CalendarListResponseParam>()

			for (i in 1..5) {
				calendarListResponseParam.add(
					CalendarListResponseParam(
						id = i.toLong(),
						userId = if (partnerId != null && i > 3) partnerId else calendarListParam.user.id!!,
						title = "test",
						memo = "test",
						startDate = LocalDate.of(calendarListParam.year!!, calendarListParam.month!!, 1),
						endDate = LocalDate.of(calendarListParam.year!!, calendarListParam.month!!, 2),
						startTime = null,
						endTime = null,
						color = Color.PRIMARY,
						alarm = Alarm.NONE
					)
				)
			}

			return calendarListResponseParam
		}

		fun getCalendarCreateRequest(): CalendarCreateRequest {
			return CalendarCreateRequest(
				title = "test",
				memo = "test",
				color = Color.PRIMARY,
				alarm = Alarm.NONE,
				startDate = LocalDate.of(2024,1, 2),
				endDate = null,
				startTime = null,
				endTime = null
			)
		}

		fun getCalendarUpdateRequest(): CalendarUpdateRequest {
			return CalendarUpdateRequest(
				title = "test",
				memo = "test",
				color = Color.PRIMARY,
				alarm = Alarm.NONE,
				startDate = LocalDate.of(2024,1, 2),
				endDate = LocalDate.of(2024, 1, 2),
				startTime = null,
				endTime = null
			)
		}
	}
}
