package com.lovebird.domain.repository.reader

import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.lovebird.domain.annotation.Reader
import com.lovebird.domain.dto.query.DiaryListRequestParam
import com.lovebird.domain.dto.query.DiaryResponseParam
import com.lovebird.domain.dto.query.DiarySimpleRequestParam
import com.lovebird.domain.dto.query.DiarySimpleResponseParam
import com.lovebird.domain.entity.Diary
import com.lovebird.domain.repository.jpa.DiaryJpaRepository
import com.lovebird.domain.repository.query.DiaryQueryRepository

@Reader
class DiaryReader(
	private val diaryJpaRepository: DiaryJpaRepository,
	private val diaryQueryRepository: DiaryQueryRepository
) {

	fun findEntityById(id: Long): Diary {
		return diaryJpaRepository.findById(id).orElseThrow { throw LbException(ReturnCode.WRONG_PARAMETER) }
	}

	fun findBeforeNowUsingCursor(param: DiaryListRequestParam): List<DiaryResponseParam> {
		return diaryQueryRepository.findBeforeNowUsingCursor(param)
	}

	fun findAfterNowUsingCursor(param: DiaryListRequestParam): List<DiaryResponseParam> {
		return diaryQueryRepository.findAfterNowUsingCursor(param)
	}

	fun findAllByMemoryDate(param: DiarySimpleRequestParam): List<DiarySimpleResponseParam> {
		return diaryQueryRepository.findAllByMemoryDate(param)
	}

	fun findAll(userId: Long, partnerId: Long?): List<DiarySimpleResponseParam> {
		return diaryQueryRepository.findAll(userId, partnerId)
	}
}
