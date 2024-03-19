package com.lovebird.api.dto.param.external

import com.lovebird.common.enums.Domain
import org.springframework.web.multipart.MultipartFile

data class ProfileImageUploadParam(
	val image: MultipartFile,
	val domain: String
) {
	companion object {

		fun from(image: MultipartFile): ProfileImageUploadParam {
			return ProfileImageUploadParam(
				image = image,
				domain = Domain.PROFILE.lower()
			)
		}
	}
}
