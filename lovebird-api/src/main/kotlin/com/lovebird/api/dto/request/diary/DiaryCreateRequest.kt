package com.lovebird.api.dto.request.diary

import com.lovebird.api.dto.param.diary.DiaryCreateParam
import com.lovebird.domain.entity.User
import java.time.LocalDate

data class DiaryCreateRequest(
	val title: String,
	val memoryDate: LocalDate,
	val place: String?,
	val content: String?,
	val imageUrls: List<String>?
) {
	fun toParam(user: User): DiaryCreateParam {
		return DiaryCreateParam(
			user = user,
			title = title,
			memoryDate = memoryDate,
			place = place,
			content = content,
			imageUrls = imageUrls
		)
	}
}
