package com.lovebird.api.util

import com.lovebird.api.dto.param.diary.DiaryCreateParam
import com.lovebird.api.dto.param.diary.DiaryUpdateParam
import com.lovebird.api.provider.AesEncryptProvider
import com.lovebird.domain.dto.query.DiaryResponseParam
import com.lovebird.domain.entity.Diary
import org.springframework.stereotype.Component

@Component
class DiaryUtils(
	private val aesEncryptProvider: AesEncryptProvider
) {

	fun decryptDiaries(diaries: List<DiaryResponseParam>) {
		diaries.forEach {
			it.title = aesEncryptProvider.decryptString(it.title)
			it.place = it.place?.let { place -> aesEncryptProvider.decryptString(place) }
			it.content = it.content?.let { content -> aesEncryptProvider.decryptString(content) }
		}
	}

	fun decryptDiariesOfSimple(diaries: List<DiaryResponseParam>) {
		diaries.forEach {
			it.title = aesEncryptProvider.decryptString(it.title)
			it.place = it.place?.let { place -> aesEncryptProvider.decryptString(place) }
			it.content = it.content?.let { content -> aesEncryptProvider.decryptString(content) }
		}
	}

	fun decryptDiary(diary: Diary) {
		diary.title = aesEncryptProvider.decryptString(diary.title)
		diary.place = diary.place?.let { aesEncryptProvider.decryptString(it) }
		diary.content = diary.content?.let { aesEncryptProvider.decryptString(it) }
	}

	fun encryptDiaryCreateParam(param: DiaryCreateParam) {
		param.title = aesEncryptProvider.encryptString(param.title)
		param.place = param.place?.let { aesEncryptProvider.encryptString(it) }
		param.content = param.content?.let { aesEncryptProvider.encryptString(it) }
	}

	fun encryptDiaryUpdateParam(param: DiaryUpdateParam) {
		param.title = aesEncryptProvider.encryptString(param.title)
		param.place = param.place?.let { aesEncryptProvider.encryptString(it) }
		param.content = param.content?.let { aesEncryptProvider.encryptString(it) }
	}
}
