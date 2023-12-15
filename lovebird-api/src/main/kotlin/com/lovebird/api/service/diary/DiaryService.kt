package com.lovebird.api.service.diary

import com.lovebird.api.dto.param.diary.DiaryCreateParam
import com.lovebird.api.dto.param.diary.DiaryUpdateParam
import com.lovebird.api.dto.request.diary.DiaryListRequest
import com.lovebird.api.dto.response.diary.DiaryDetailResponse
import com.lovebird.api.dto.response.diary.DiaryListResponse
import com.lovebird.api.dto.response.diary.DiarySimpleListResponse
import com.lovebird.api.service.couple.CoupleService
import com.lovebird.common.enums.DiarySearchType
import com.lovebird.domain.dto.query.DiaryListRequestParam
import com.lovebird.domain.dto.query.DiaryResponseParam
import com.lovebird.domain.entity.Diary
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.reader.DiaryReader
import com.lovebird.domain.repository.writer.DiaryWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DiaryService(
	private val diaryReader: DiaryReader,
	private val diaryWriter: DiaryWriter,
	private val diaryImageService: DiaryImageService,
	private val coupleService: CoupleService
) {

	@Transactional(readOnly = true)
	fun findPageByCursor(request: DiaryListRequest.SearchByCursorRequest, user: User): DiaryListResponse {
		val partner: User? = coupleService.findPartnerByUser(user)

		return when (request.searchType) {
			DiarySearchType.BEFORE -> {
				findBeforeNowUsingCursor(request.toParam(user.id!!, partner?.id))
			}
			DiarySearchType.AFTER -> {
				findAfterNowUsingCursor(request.toParam(user.id!!, partner?.id))
			}
		}
	}

	@Transactional
	fun save(param: DiaryCreateParam) {
		val diary: Diary = diaryWriter.save(param.toEntity())
		param.imageUrls?.let { diaryImageService.saveAll(diary, it) }
	}

	@Transactional
	fun update(param: DiaryUpdateParam) {
		val diary: Diary = diaryReader.findEntityById(param.diaryId)
		diaryWriter.update(diary, param.toDomainParam())

		param.imageUrls?.let {
			diaryImageService.updateAll(diary, it)
		}
	}

	@Transactional
	fun delete(diaryId: Long) {
		val diary: Diary = diaryReader.findEntityById(diaryId)
		diaryImageService.deleteAll(diary)
		diaryWriter.delete(diary)
	}

	@Transactional(readOnly = true)
	fun findAllByMemoryDate(request: DiaryListRequest.SearchByMemoryDateRequest, user: User): DiarySimpleListResponse {
		val partner: User? = coupleService.findPartnerByUser(user)

		return DiarySimpleListResponse(diaryReader.findAllByMemoryDate(request.toParam(user.id!!, partner?.id)))
	}

	@Transactional(readOnly = true)
	fun findDetailById(diaryId: Long): DiaryDetailResponse {
		val diary: Diary = diaryReader.findEntityById(diaryId)
		return DiaryDetailResponse.of(diary)
	}

	private fun findBeforeNowUsingCursor(param: DiaryListRequestParam): DiaryListResponse {
		val diaries: List<DiaryResponseParam> = diaryReader.findBeforeNowUsingCursor(param)
		return DiaryListResponse(diaries)
	}

	private fun findAfterNowUsingCursor(param: DiaryListRequestParam): DiaryListResponse {
		val diaries: List<DiaryResponseParam> = diaryReader.findAfterNowUsingCursor(param)
		return DiaryListResponse(diaries)
	}
}
