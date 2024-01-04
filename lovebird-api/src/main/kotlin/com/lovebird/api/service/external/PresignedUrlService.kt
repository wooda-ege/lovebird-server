package com.lovebird.api.service.external

import com.lovebird.api.dto.param.external.DiaryUploadPresignedUrlParam
import com.lovebird.api.dto.param.external.ProfileUploadPresignedUrlParam
import com.lovebird.api.dto.response.external.PresignedUrlListResponse
import com.lovebird.api.dto.response.external.PresignedUrlResponse
import com.lovebird.common.enums.Domain
import com.lovebird.common.util.FilenameUtils
import com.lovebird.s3.provider.PresignedUrlProvider
import org.springframework.stereotype.Service

@Service
class PresignedUrlService(
	private val presignedUrlProvider: PresignedUrlProvider
) {

	fun getDiaryPresignedUrls(param: DiaryUploadPresignedUrlParam): PresignedUrlListResponse {
		val newFileNames = FilenameUtils.generateDiaryImageNames(param.filenames, param.userId, param.diaryId)

		return PresignedUrlListResponse.of(newFileNames.map { getDiaryPresignedUrl(param.userId, it) })
	}

	fun getProfilePresignedUrl(param: ProfileUploadPresignedUrlParam): PresignedUrlResponse {
		val newFilename: String = FilenameUtils.generateProfileImageName(param.filename, param.userId)
		val presignedUrl = presignedUrlProvider.getUploadPresignedUrl(Domain.PROFILE.lower(), param.userId, newFilename)

		return PresignedUrlResponse(presignedUrl, newFilename)
	}

	private fun getDiaryPresignedUrl(userId: Long, filename: String): PresignedUrlResponse {
		val presignedUrl = presignedUrlProvider.getUploadPresignedUrl(Domain.DIARY.lower(), userId, filename)
		return PresignedUrlResponse(presignedUrl, filename)
	}
}
