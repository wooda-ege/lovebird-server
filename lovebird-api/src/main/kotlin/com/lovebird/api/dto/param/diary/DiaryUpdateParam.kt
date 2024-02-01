package com.lovebird.api.dto.param.diary

import com.lovebird.api.provider.AesEncryptProvider.encryptString
import com.lovebird.domain.dto.command.DiaryUpdateRequestParam
import java.time.LocalDate

data class DiaryUpdateParam(
	val diaryId: Long,
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

	fun toDomainParam(): DiaryUpdateRequestParam {
		return DiaryUpdateRequestParam(
			title = title,
			memoryDate = memoryDate,
			place = place,
			content = content
		)
	}
}
