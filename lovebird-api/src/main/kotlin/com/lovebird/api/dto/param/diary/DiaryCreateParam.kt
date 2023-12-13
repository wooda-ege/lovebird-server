package com.lovebird.api.dto.param.diary

import com.lovebird.domain.entity.Diary
import com.lovebird.domain.entity.User
import java.time.LocalDate

data class DiaryCreateParam(
	val user: User,
	val title: String,
	val memoryDate: LocalDate,
	val place: String?,
	val content: String?,
	val imageUrls: List<String>?
) {
	fun toEntity(): Diary {
		return Diary(
			user = user,
			title = title,
			memoryDate = memoryDate,
			place = place,
			content = content
		)
	}
}
