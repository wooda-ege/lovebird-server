package com.lovebird.api.dto.param.diary

import com.lovebird.api.provider.AesEncryptProvider.encryptString
import com.lovebird.domain.entity.Diary
import com.lovebird.domain.entity.User
import java.time.LocalDate

data class DiaryCreateParam(
	val user: User,
	var title: String,
	val memoryDate: LocalDate,
	var place: String?,
	var content: String?,
	val imageUrls: List<String>?
) {
	fun encrypt() {
		this.title = encryptString(this.title)
		this.place = this.place?.let { encryptString(it) }
		this.content = this.content?.let { encryptString(it) }
	}

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
