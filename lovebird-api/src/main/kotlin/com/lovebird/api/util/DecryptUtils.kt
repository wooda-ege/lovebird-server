package com.lovebird.api.util

import com.lovebird.api.provider.AesEncryptProvider.decryptString
import com.lovebird.domain.dto.query.DiaryResponseParam
import org.springframework.stereotype.Component

@Component
object DecryptUtils {

	fun decryptDiaries(diaries: List<DiaryResponseParam>) {
		diaries.forEach {
			it.title = decryptString(it.title)
			it.place = it.place?.let { place -> decryptString(place) }
			it.content = it.content?.let { content -> decryptString(content) }
		}
	}
}
