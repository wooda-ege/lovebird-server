package com.lovebird.domain.repository.reader

import com.lovebird.common.annotation.Reader
import com.lovebird.domain.dto.query.DiaryListRequestParam
import com.lovebird.domain.dto.query.DiaryResponseParam
import com.lovebird.domain.entity.Diary
import com.lovebird.domain.repository.jpa.DiaryJpaRepository
import com.lovebird.domain.repository.query.DiaryQueryRepository
import jakarta.persistence.EntityNotFoundException

@Reader
class DiaryReader(
	private val diaryJpaRepository: DiaryJpaRepository,
	private val diaryQueryRepository: DiaryQueryRepository
) {

	fun findEntityById(id: Long): Diary {
		return diaryJpaRepository.findById(id).orElseThrow { throw EntityNotFoundException() }
	}

	fun findBeforeNowUsingCursor(param: DiaryListRequestParam): List<DiaryResponseParam> {
		return diaryQueryRepository.findBeforeNowUsingCursor(param)
	}

	fun findAfterNowUsingCursor(param: DiaryListRequestParam): List<DiaryResponseParam> {
		return diaryQueryRepository.findAfterNowUsingCursor(param)
	}
}
