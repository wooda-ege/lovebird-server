package com.lovebird.domain.repository.writer

import com.lovebird.domain.annotation.Writer
import com.lovebird.domain.dto.command.DiaryUpdateRequestParam
import com.lovebird.domain.entity.Diary
import com.lovebird.domain.repository.jpa.DiaryJpaRepository

@Writer
class DiaryWriter(
	private val diaryJpaRepository: DiaryJpaRepository
) {

	fun save(diary: Diary): Diary {
		return diaryJpaRepository.save(diary)
	}

	fun update(diary: Diary, param: DiaryUpdateRequestParam) {
		diary.update(param)
	}

	fun delete(diary: Diary) {
		diaryJpaRepository.delete(diary)
	}

	fun deleteAll(diaries: List<Diary>) {
		diaryJpaRepository.deleteAll(diaries)
	}
}
