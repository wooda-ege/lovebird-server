package com.lovebird.service.external

import com.lovebird.common.utils.FilenameFormatter
import com.lovebird.dto.param.DiaryUploadPresignedUrlParam
import com.lovebird.dto.param.ProfileUploadPresignedUrlParam
import com.lovebird.dto.response.PresignedUrlListResponse
import com.lovebird.dto.response.PresignedUrlResponse
import com.lovebird.provider.PresignedUrlProvider
import org.springframework.stereotype.Service


@Service
class PresignedUrlService(
	private val presignedUrlProvider: PresignedUrlProvider,
	private val fileNameFormatter: FilenameFormatter
) {

	fun getDiaryPresignedUrls(param: DiaryUploadPresignedUrlParam): PresignedUrlListResponse {
		val newFileNames = fileNameFormatter.generateDiaryImageNames(param.filenames, param.userId, param.diaryId)

		return PresignedUrlListResponse.of(newFileNames.map { getDiaryPresignedUrl(param.userId, it) })
	}

	fun getProfilePresignedUrl(param: ProfileUploadPresignedUrlParam): PresignedUrlResponse {
		val newFilename: String = fileNameFormatter.generateProfileImageName(param.filename, param.userId)
		val presignedUrl = presignedUrlProvider.getUploadPresignedUrl("profile", param.userId, newFilename)

		return PresignedUrlResponse(presignedUrl, newFilename)
	}

	private fun getDiaryPresignedUrl(userId: Long, filename: String): PresignedUrlResponse {
		val presignedUrl = presignedUrlProvider.getUploadPresignedUrl("diary", userId, filename)
		return PresignedUrlResponse(presignedUrl, filename)
	}
}
