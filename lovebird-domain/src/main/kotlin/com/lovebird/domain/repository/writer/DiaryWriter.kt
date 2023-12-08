package com.lovebird.domain.repository.writer

import com.lovebird.common.annotation.Writer
import com.lovebird.domain.dto.command.DiaryUpdateRequestParam
import com.lovebird.domain.entity.Diary
import com.lovebird.domain.repository.jpa.DiaryJpaRepository

@Writer
class DiaryWriter(
	private val diaryJpaRepository: DiaryJpaRepository
) {

	fun save(diary: Diary) {
		diaryJpaRepository.save(diary)
	}

	fun update(diary: Diary, param: DiaryUpdateRequestParam) {
		diary.update(param)
	}

	fun delete(diary: Diary) {
		diaryJpaRepository.delete(diary)
	}
}
