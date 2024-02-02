package com.lovebird.api.dto.response.diary

import com.lovebird.domain.entity.Diary
import java.time.LocalDate

data class DiaryDetailResponse(
	val diaryId: Long,
	val userId: Long,
	val title: String,
	val memoryDate: LocalDate,
	val place: String?,
	val content: String?,
	val imageUrls: List<String>
) {
	companion object {

		@JvmStatic
		fun of(entity: Diary): DiaryDetailResponse {
			return DiaryDetailResponse(
				diaryId = entity.id!!,
				userId = entity.user.id!!,
				title = entity.title,
				memoryDate = entity.memoryDate,
				place = entity.place,
				content = entity.content,
				imageUrls = entity.diaryImages.map { it.imageUrl }
			)
		}
	}
}
