package com.lovebird.api.service.diary

import com.lovebird.api.dto.param.diary.DiaryCreateParam
import com.lovebird.api.dto.param.diary.DiaryUpdateParam
import com.lovebird.api.dto.response.diary.DiaryDetailResponse
import com.lovebird.api.dto.response.diary.DiaryListResponse
import com.lovebird.domain.dto.query.DiaryListRequestParam
import com.lovebird.domain.entity.Diary
import com.lovebird.domain.repository.reader.DiaryReader
import com.lovebird.domain.repository.writer.DiaryWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DiaryService(
	private val diaryReader: DiaryReader,
	private val diaryWriter: DiaryWriter,
	private val diaryImageService: DiaryImageService
) {

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
	fun findBeforeNowUsingCursor(param: DiaryListRequestParam): DiaryListResponse {
		val diaries: List<DiaryDetailResponse> = diaryReader.findBeforeNowUsingCursor(param).map { DiaryDetailResponse.of(it) }
		return DiaryListResponse(diaries)
	}

	@Transactional(readOnly = true)
	fun findAfterNowUsingCursor(param: DiaryListRequestParam): DiaryListResponse {
		val diaries: List<DiaryDetailResponse> = diaryReader.findAfterNowUsingCursor(param).map { DiaryDetailResponse.of(it) }
		return DiaryListResponse(diaries)
	}

	@Transactional(readOnly = true)
	fun findDetailById(diaryId: Long): DiaryDetailResponse {
		val diary: Diary = diaryReader.findEntityById(diaryId)
		return DiaryDetailResponse.of(diary)
	}
}
