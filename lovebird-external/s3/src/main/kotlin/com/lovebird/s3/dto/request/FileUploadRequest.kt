package com.lovebird.s3.dto.request

import org.springframework.web.multipart.MultipartFile

data class FileUploadRequest(
	val file: MultipartFile,
	val fileName: String,
	val domain: String,
	val userId: Long?
)
