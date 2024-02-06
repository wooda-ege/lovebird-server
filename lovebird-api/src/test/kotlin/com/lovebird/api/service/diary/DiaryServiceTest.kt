package com.lovebird.api.service.diary

import com.lovebird.api.common.base.ServiceDescribeSpec
import com.lovebird.api.dto.response.diary.DiaryDetailResponse
import com.lovebird.api.dto.response.diary.DiaryListResponse
import com.lovebird.api.dto.response.diary.DiarySimpleListResponse
import com.lovebird.api.util.DiaryUtils
import com.lovebird.api.utils.CoupleTestFixture.getCoupleEntry
import com.lovebird.api.utils.DiaryTestFixture
import com.lovebird.common.enums.DiarySearchType
import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.lovebird.domain.repository.reader.CoupleEntryReader
import com.lovebird.domain.repository.reader.DiaryReader
import com.lovebird.domain.repository.writer.DiaryImageWriter
import com.lovebird.domain.repository.writer.DiaryWriter
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.should
import io.kotest.matchers.string.startWith
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import java.time.LocalDate

class DiaryServiceTest : ServiceDescribeSpec({
	val diaryReader = mockk<DiaryReader>(relaxed = true)
	val diaryWriter = mockk<DiaryWriter>(relaxed = true)
	val diaryImageWriter = mockk<DiaryImageWriter>(relaxed = true)
	val coupleEntryReader = mockk<CoupleEntryReader>(relaxed = true)
	mockkObject(DiaryUtils)
	val diaryService = DiaryService(
		diaryReader = diaryReader,
		diaryWriter = diaryWriter,
		diaryImageWriter = diaryImageWriter,
		coupleEntryReader = coupleEntryReader
	)

	afterEach {
		clearAllMocks()
	}

	describe("다이어리 커서 기반 페이지네이션") {
		val coupleEntry = getCoupleEntry()
		val user = coupleEntry.user
		val pageSize: Long = 5

		context("현재 커서 기반 이전 다이어리 요청이라면") {
			val searchByCursorRequest = DiaryTestFixture.getSearchByCursorRequest(DiarySearchType.BEFORE, -1, pageSize)
			val diaries = DiaryTestFixture.getDiaries(pageSize)
			every { coupleEntryReader.findByUser(user) } returns coupleEntry
			every { diaryReader.findBeforeNowUsingCursor(any()) } returns diaries
			every { DiaryUtils.decryptDiaries(any()) } just Runs

			it("커서 기준 이전 다이어리들을 호출한다") {
				// 상태 검증
				diaryService.findPageByCursor(searchByCursorRequest, user)
					.shouldBeEqualToComparingFields(DiaryListResponse.of(diaries))

				// 행위 검증
				verify(exactly = 1) {
					coupleEntryReader.findByUser(user)
					diaryReader.findBeforeNowUsingCursor(any())
					DiaryUtils.decryptDiaries(diaries)
				}
			}
		}

		context("현재 커서 기반 이후 다이어리 요청이라면") {
			val searchByCursorRequest = DiaryTestFixture.getSearchByCursorRequest(DiarySearchType.AFTER, -1, pageSize)
			val diaries = DiaryTestFixture.getDiaries(pageSize)
			every { coupleEntryReader.findByUser(user) } returns getCoupleEntry()
			every { diaryReader.findAfterNowUsingCursor(any()) } returns diaries
			every { DiaryUtils.decryptDiaries(any()) } just Runs

			it("커서 기준 이후 다이어리들을 호출한다.") {
				// 상태 검증
				diaryService.findPageByCursor(searchByCursorRequest, user)
					.shouldBeEqualToComparingFields(DiaryListResponse.of(diaries))

				// 행위 검증
				verify(exactly = 1) {
					coupleEntryReader.findByUser(user)
					diaryReader.findAfterNowUsingCursor(any())
					DiaryUtils.decryptDiaries(diaries)
				}
			}
		}
	}

	describe("다이어리를 저장할때") {
		val user = getUser(1L)

		context("이미지들이 존재한다면") {
			val imageUrls = arrayListOf("imageUrl1", "imageUrl2", "imageUrl3")
			val param = DiaryTestFixture.getCreateParam(imageUrls = imageUrls, user = user)
			val diary = DiaryTestFixture.getDiaryByCreateParam(param, user)
			every { diaryWriter.save(any()) } returns diary
			every { diaryImageWriter.saveAll(diary, imageUrls) } just Runs
			every { DiaryUtils.encryptDiaryCreateParam(any()) } just Runs

			it("다이어리와 이미지 모두 저장한다") {
				diaryService.save(param)

				// 행위 검증
				verify(exactly = 1) {
					DiaryUtils.encryptDiaryCreateParam(param)
					diaryWriter.save(any())
					diaryImageWriter.saveAll(diary, imageUrls)
				}
			}
		}

		context("이미지들이 존재하지 않는다면") {
			val param = DiaryTestFixture.getCreateParam(imageUrls = null, user = user)
			val diary = DiaryTestFixture.getDiaryByCreateParam(param, user)
			every { diaryWriter.save(any()) } returns diary
			every { DiaryUtils.encryptDiaryCreateParam(any()) } just Runs

			it("다이어리만 저장한다") {
				diaryService.save(param)

				// 행위 검증
				verify(exactly = 1) {
					DiaryUtils.encryptDiaryCreateParam(param)
					diaryWriter.save(any())
				}
			}
		}
	}

	describe("다이어리를 업데이트 할 때") {
		val user = getUser(1L)

		context("다이어리 id 가 존재하지 않는다면") {
			val param = DiaryTestFixture.getUpdateParam(null)
			every { diaryReader.findEntityById(param.diaryId) } throws LbException(ReturnCode.WRONG_PARAMETER)

			it("예외가 발생한다") {
				val exception = shouldThrow<LbException> {
					diaryService.update(param)
				}

				// 예외 상태 검증
				exception.getMsg() should startWith(ReturnCode.WRONG_PARAMETER.message)
				exception.getCode() should startWith(ReturnCode.WRONG_PARAMETER.code)

				// 행위 검증
				verify(exactly = 1) {
					diaryReader.findEntityById(param.diaryId)
				}
			}
		}

		context("다이어리 이미지들이 존재하지 않는다면") {
			val param = DiaryTestFixture.getUpdateParam(null)
			val diary = DiaryTestFixture.getDiaryByUpdateParam(param = param, user = user)
			every { diaryReader.findEntityById(param.diaryId) } returns diary
			every { DiaryUtils.encryptDiaryUpdateParam(param) } just Runs
			every { diaryWriter.update(diary, any()) } just Runs

			it("다이어리만 업데이트 한다") {
				diaryService.update(param)

				// 행위 검증
				verify(exactly = 1) {
					diaryReader.findEntityById(param.diaryId)
					DiaryUtils.encryptDiaryUpdateParam(param)
					diaryWriter.update(diary, any())
				}
			}
		}

		context("다이어리와 이미지들이 모두 존재한다면") {
			val imageUrls = arrayListOf("imageUrl1", "imageUrl2", "imageUrl3")
			val param = DiaryTestFixture.getUpdateParam(imageUrls = imageUrls)
			val diary = DiaryTestFixture.getDiaryByUpdateParam(param = param, user = user)
			every { diaryReader.findEntityById(param.diaryId) } returns diary
			every { DiaryUtils.encryptDiaryUpdateParam(param) } just Runs
			every { diaryWriter.update(diary, any()) } just Runs
			every { diaryImageWriter.deleteAll(diary) } just Runs
			every { diaryImageWriter.saveAll(diary, imageUrls) } just Runs

			it("다이어리와 이미지 모두 업데이트 한다") {
				diaryService.update(param)

				// 행위 검증
				verify(exactly = 1) {
					diaryReader.findEntityById(param.diaryId)
					DiaryUtils.encryptDiaryUpdateParam(param)
					diaryWriter.update(diary, any())
					diaryImageWriter.deleteAll(diary)
					diaryImageWriter.saveAll(diary, imageUrls)
				}
			}
		}
	}

	describe("다이어리를 삭제할때") {
		val user = getUser(1L)

		context("다이어리 id가 존재하지 않는다면") {
			every { diaryReader.findEntityById(1L) } throws LbException(ReturnCode.WRONG_PARAMETER)

			it("예외가 발생한다") {
				val exception = shouldThrow<LbException> {
					diaryService.delete(1L)
				}

				// 예외 상태 검증
				exception.getMsg() should startWith(ReturnCode.WRONG_PARAMETER.message)
				exception.getCode() should startWith(ReturnCode.WRONG_PARAMETER.code)

				// 행위 검증
				verify(exactly = 1) {
					diaryReader.findEntityById(1L)
				}
			}
		}

		context("다이어리 id가 존재한다면") {
			val diary = DiaryTestFixture.getDiaryByUser(user)
			every { diaryReader.findEntityById(1L) } returns diary
			every { diaryImageWriter.deleteAll(diary) } just Runs
			every { diaryWriter.delete(diary) } just Runs

			it("다이어리와 이미지를 모두 삭제한다") {
				diaryService.delete(1L)

				// 행위 검증
				verify(exactly = 1) {
					diaryReader.findEntityById(1L)
					diaryImageWriter.deleteAll(diary)
					diaryWriter.delete(diary)
				}
			}
		}
	}

	describe("다이어리를 MemoryDate 기준으로 모두 조회할때") {
		val request = DiaryTestFixture.getSearchByMemoryDateRequest(memoryDate = LocalDate.now())
		val user = getUser(1L)
		val partner = getUser(2L)
		val size = 5

		context("파트너가 존재하지 않아도") {
			val diaries = DiaryTestFixture.getDiarySimpleResponseList(user = user, partner = null, size = size)
			every { coupleEntryReader.findByUser(user) } returns getCoupleEntry(user = user, partner = user)
			every { diaryReader.findAllByMemoryDate(any()) } returns diaries
			every { DiaryUtils.decryptDiariesOfSimple(any()) } just Runs

			it("조회에 성공한다") {

				// 상태 검증
				diaryService.findAllByMemoryDate(request, user)
					.shouldBeEqualToComparingFields(DiarySimpleListResponse.of(diaries))

				// 행위 검증
				verify(exactly = 1) {
					coupleEntryReader.findByUser(user)
					diaryReader.findAllByMemoryDate(any())
					DiaryUtils.decryptDiariesOfSimple(any())
				}
			}
		}

		context("파트너가 존재한다면") {
			val diaries = DiaryTestFixture.getDiarySimpleResponseList(user = user, partner = partner, size = size)
			every { coupleEntryReader.findByUser(user) } returns getCoupleEntry(user = user, partner = partner)
			every { diaryReader.findAllByMemoryDate(any()) } returns diaries
			every { DiaryUtils.decryptDiariesOfSimple(any()) } just Runs

			it("조회에 성공한다") {

				// 상태 검증
				diaryService.findAllByMemoryDate(request, user)
					.shouldBeEqualToComparingFields(DiarySimpleListResponse.of(diaries))

				// 행위 검증
				verify(exactly = 1) {
					coupleEntryReader.findByUser(user)
					diaryReader.findAllByMemoryDate(any())
					DiaryUtils.decryptDiariesOfSimple(any())
				}
			}
		}
	}

	describe("다이어리 id로 상세 조회한다면") {
		context("id가 존재하지 않는다면") {
			every { diaryReader.findEntityById(1L) } throws LbException(ReturnCode.WRONG_PARAMETER)

			it("예외가 발생한다") {
				val exception = shouldThrow<LbException> {
					diaryService.findDetailById(1L)
				}

				// 예외 상태 검증
				exception.getMsg() should startWith(ReturnCode.WRONG_PARAMETER.message)
				exception.getCode() should startWith(ReturnCode.WRONG_PARAMETER.code)

				// 행위 검증
				verify(exactly = 1) {
					diaryReader.findEntityById(1L)
				}
			}
		}

		context("id가 존재한다면") {
			val user = getUser(1L)
			val diary = DiaryTestFixture.getDiaryByUser(user)
			every { DiaryUtils.decryptDiary(diary) } just Runs
			every { diaryReader.findEntityById(diary.id!!) } returns diary

			it("다이어리 상세 조회에 성공한다") {

				// 상태 검증
				diaryService.findDetailById(diary.id!!).shouldBeEqualToComparingFields(
					DiaryDetailResponse.of(diary)
				)

				// 행위 검증
				verify(exactly = 1) {
					diaryReader.findEntityById(diary.id!!)
					DiaryUtils.decryptDiary(diary)
				}
			}
		}
	}
})
