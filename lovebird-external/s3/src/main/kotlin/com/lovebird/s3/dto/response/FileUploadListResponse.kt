package com.lovebird.s3.dto.response

data class FileUploadListResponse(
	val fileUrls: List<FileUploadResponse>,
	val totalCount: Int
) {
	companion object {

		fun from(fileUrls: List<FileUploadResponse>): FileUploadListResponse {
			return FileUploadListResponse(fileUrls, fileUrls.size)
		}
	}
}
