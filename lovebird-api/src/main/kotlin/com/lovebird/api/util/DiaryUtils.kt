package com.lovebird.api.util

import com.lovebird.api.dto.param.diary.DiaryCreateParam
import com.lovebird.api.provider.AesEncryptProvider.decryptString
import com.lovebird.api.provider.AesEncryptProvider.encryptString
import com.lovebird.domain.dto.query.DiaryResponseParam
import org.springframework.stereotype.Component

@Component
object DiaryUtils {

	fun decryptDiaries(diaries: List<DiaryResponseParam>) {
		diaries.forEach {
			it.title = decryptString(it.title)
			it.place = it.place?.let { place -> decryptString(place) }
			it.content = it.content?.let { content -> decryptString(content) }
		}
	}

	fun encryptDiaryCreateParam(param: DiaryCreateParam) {
		param.title = encryptString(param.title)
		param.place = param.place?.let { encryptString(it) }
		param.content = param.content?.let { encryptString(it) }
	}
}
