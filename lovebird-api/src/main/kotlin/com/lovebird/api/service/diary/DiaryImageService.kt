package com.lovebird.api.service.diary

import com.lovebird.domain.entity.Diary
import com.lovebird.domain.repository.writer.DiaryImageWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DiaryImageService(
	private val diaryImageWriter: DiaryImageWriter
) {

	@Transactional
	fun saveAll(diary: Diary, imageUrls: List<String>) {
		diaryImageWriter.saveAll(diary, imageUrls)
	}

	@Transactional
	fun updateAll(diary: Diary, imageUrls: List<String>) {
		// TODO: 2023/12/12 : komment : 일기 작성 폼 변경 후 refactoring 예정
		diaryImageWriter.deleteAll(diary)
		diaryImageWriter.saveAll(diary, imageUrls)
	}

	@Transactional
	fun deleteAll(diary: Diary) {
		diaryImageWriter.deleteAll(diary)
	}
}
