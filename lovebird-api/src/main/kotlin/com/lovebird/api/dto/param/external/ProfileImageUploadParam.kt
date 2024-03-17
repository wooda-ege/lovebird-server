package com.lovebird.api.dto.param.external

import org.springframework.web.multipart.MultipartFile

data class ProfileImageUploadParam(
	val image: MultipartFile,
	val domain: String,
	val providerId: String
)
