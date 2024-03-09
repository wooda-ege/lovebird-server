package com.lovebird.domain.dto.query

import com.lovebird.domain.entity.Diary
import java.time.LocalDate

data class DiaryResponseParam(
	val diaryId: Long,
	val userId: Long,
	var title: String,
	val memoryDate: LocalDate,
	var place: String?,
	var content: String?,
	val imageUrls: List<String>?
) {

	companion object {
		fun of(transform: Map<Diary, List<String>>): List<DiaryResponseParam> {
			return transform.entries.stream()
				.map { entry ->
					DiaryResponseParam(
						diaryId = entry.key.id!!,
						userId = entry.key.user.id!!,
						title = entry.key.title,
						memoryDate = entry.key.memoryDate,
						place = entry.key.place,
						content = entry.key.content,
						imageUrls = entry.value
					)
				}.toList()
		}
	}
}
