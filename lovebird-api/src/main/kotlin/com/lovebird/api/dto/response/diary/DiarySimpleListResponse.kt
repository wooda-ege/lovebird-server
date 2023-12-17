package com.lovebird.api.dto.response.diary

import com.lovebird.api.provider.AesEncryptProvider
import com.lovebird.domain.dto.query.DiarySimpleResponseParam

data class DiarySimpleListResponse(
	val diaries: List<DiarySimpleResponseParam>,
	val totalCount: Int = diaries.size
) {
	companion object {
		fun of(diaries: List<DiarySimpleResponseParam>): DiarySimpleListResponse {
			diaries.forEach {
				it.title = AesEncryptProvider.decryptString(it.title)
				it.place = it.place?.let { place -> AesEncryptProvider.decryptString(place) }
				it.content = it.content?.let { content -> AesEncryptProvider.decryptString(content) }
			}

			return DiarySimpleListResponse(diaries)
		}
	}
}
