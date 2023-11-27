package com.lovebird.dto.response

data class PresignedUrlListResponse(
	val presignedUrls: List<PresignedUrlResponse>,
	val totalCount: Int
) {
	companion object {
		@JvmStatic
		fun of(presignedUrlResponses: List<PresignedUrlResponse>): PresignedUrlListResponse {
			return PresignedUrlListResponse(presignedUrlResponses, presignedUrlResponses.size)
		}
	}
}
