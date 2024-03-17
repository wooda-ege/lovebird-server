package com.lovebird.s3.dto.request

import org.springframework.web.multipart.MultipartFile

data class FilesUploadRequest(
	val files: List<MultipartFile>,
	val fileNames: List<String>,
	val domain: String,
	val userId: Long?
)
