package com.lovebird.api.dto.param.external

import com.lovebird.common.enums.Domain
import org.springframework.web.multipart.MultipartFile

data class DiaryImagesUploadParam(
	val images: List<MultipartFile>,
	val domain: String,
	val userId: Long
) {
	companion object {

		fun of(images: List<MultipartFile>, userId: Long): DiaryImagesUploadParam {
			return DiaryImagesUploadParam(
				images = images,
				domain = Domain.DIARY.lower(),
				userId = userId
			)
		}
	}
}
