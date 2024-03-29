package com.lovebird.api.dto.response.external

data class PresignedUrlListResponse(
	val presignedUrls: List<PresignedUrlResponse>,
	val totalCount: Int
) {
	companion object {

		fun of(presignedUrlResponses: List<PresignedUrlResponse>): PresignedUrlListResponse {
			return PresignedUrlListResponse(presignedUrlResponses, presignedUrlResponses.size)
		}
	}
}
