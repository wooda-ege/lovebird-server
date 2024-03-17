package com.lovebird.api.dto.request.external

import com.lovebird.api.dto.param.external.ProfileImageUploadParam
import com.lovebird.common.enums.Domain
import org.springframework.web.multipart.MultipartFile

data class ProfileImageUploadRequest(
	val providerId: String
) {
	fun toParam(image: MultipartFile): ProfileImageUploadParam {
		return ProfileImageUploadParam(
			image = image,
			domain = Domain.PROFILE.lower(),
			providerId = providerId
		)
	}
}
