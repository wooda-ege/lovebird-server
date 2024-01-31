package com.lovebird.api.service.diary

import com.lovebird.api.dto.param.diary.DiaryCreateParam
import com.lovebird.api.dto.param.diary.DiaryUpdateParam
import com.lovebird.api.dto.request.diary.DiaryListRequest
import com.lovebird.api.dto.response.diary.DiaryDetailResponse
import com.lovebird.api.dto.response.diary.DiaryListResponse
import com.lovebird.api.dto.response.diary.DiarySimpleListResponse
import com.lovebird.api.provider.AesEncryptProvider.decryptString
import com.lovebird.api.util.DecryptUtils
import com.lovebird.common.enums.DiarySearchType
import com.lovebird.domain.dto.query.DiaryListRequestParam
import com.lovebird.domain.dto.query.DiaryResponseParam
import com.lovebird.domain.dto.query.DiarySimpleResponseParam
import com.lovebird.domain.entity.CoupleEntry
import com.lovebird.domain.entity.Diary
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.reader.CoupleEntryReader
import com.lovebird.domain.repository.reader.DiaryReader
import com.lovebird.domain.repository.writer.DiaryWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DiaryService(
	private val diaryReader: DiaryReader,
	private val diaryWriter: DiaryWriter,
	private val diaryImageService: DiaryImageService,
	private val coupleEntryReader: CoupleEntryReader,
	private val decryptUtils: DecryptUtils
) {

	@Transactional(readOnly = true)
	fun findPageByCursor(request: DiaryListRequest.SearchByCursorRequest, user: User): DiaryListResponse {
		val coupleEntry: CoupleEntry? = coupleEntryReader.findByUser(user)
		val partner: User? = coupleEntry?.partner

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
		param.encrypt()

		val diary: Diary = diaryWriter.save(param.toEntity())
		param.imageUrls?.let { diaryImageService.saveAll(diary, it) }
	}

	@Transactional
	fun update(param: DiaryUpdateParam) {
		val diary: Diary = diaryReader.findEntityById(param.diaryId)

		param.encrypt()
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
		val coupleEntry: CoupleEntry? = coupleEntryReader.findByUser(user)
		val partner: User? = coupleEntry?.partner
		val diaries: List<DiarySimpleResponseParam> = diaryReader.findAllByMemoryDate(request.toParam(user.id!!, partner?.id))

		diaries.forEach {
			it.title = decryptString(it.title)
			it.place = it.place?.let { place -> decryptString(place) }
			it.content = it.content?.let { content -> decryptString(content) }
		}

		return DiarySimpleListResponse.of(diaries)
	}

	@Transactional(readOnly = true)
	fun findDetailById(diaryId: Long): DiaryDetailResponse {
		val diary: Diary = diaryReader.findEntityById(diaryId)
		return DiaryDetailResponse.of(diary)
	}

	private fun findBeforeNowUsingCursor(param: DiaryListRequestParam): DiaryListResponse {
		val diaries: List<DiaryResponseParam> = diaryReader.findBeforeNowUsingCursor(param)
		decryptUtils.decryptDiaries(diaries)

		return DiaryListResponse.of(diaries)
	}

	private fun findAfterNowUsingCursor(param: DiaryListRequestParam): DiaryListResponse {
		val diaries: List<DiaryResponseParam> = diaryReader.findAfterNowUsingCursor(param)
		decryptUtils.decryptDiaries(diaries)

		return DiaryListResponse.of(diaries)
	}
}
