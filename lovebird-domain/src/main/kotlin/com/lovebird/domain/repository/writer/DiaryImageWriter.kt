package com.lovebird.domain.repository.writer

import com.lovebird.domain.annotation.Writer
import com.lovebird.domain.entity.Diary
import com.lovebird.domain.entity.DiaryImage
import com.lovebird.domain.repository.jpa.DiaryImageJpaRepository

@Writer
class DiaryImageWriter(
	private val diaryImageJpaRepository: DiaryImageJpaRepository
) {

	fun saveAll(diary: Diary, imageUrls: List<String>) {
		diaryImageJpaRepository.saveAll(imageUrls.map { DiaryImage(diary, it) })
	}

	fun deleteAll(diary: Diary) {
		diaryImageJpaRepository.deleteAll(diary.diaryImages)
	}

	fun deleteAllByDiaries(diaries: List<Diary>) {
		diaryImageJpaRepository.deleteAllByDiaryIn(diaries)
	}
}
