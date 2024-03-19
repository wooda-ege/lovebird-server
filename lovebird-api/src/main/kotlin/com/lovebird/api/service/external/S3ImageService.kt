package com.lovebird.api.service.external

import com.lovebird.api.dto.param.external.DiaryImagesUploadParam
import com.lovebird.api.dto.param.external.ProfileImageUploadParam
import com.lovebird.common.util.FilenameUtils.generateDiaryImageNames
import com.lovebird.common.util.FilenameUtils.generateProfileImageName
import com.lovebird.s3.dto.request.FileUploadRequest
import com.lovebird.s3.dto.request.FilesUploadRequest
import com.lovebird.s3.dto.response.FileUploadListResponse
import com.lovebird.s3.dto.response.FileUploadResponse
import com.lovebird.s3.service.FileUploadService
import org.springframework.stereotype.Service

@Service
class S3ImageService(
	private val fileUploadService: FileUploadService
) {

	fun uploadProfileImage(param: ProfileImageUploadParam): FileUploadResponse {
		val newFileName: String = generateProfileImageName(param.image.originalFilename!!)

		return fileUploadService.upload(FileUploadRequest(param.image, newFileName, param.domain, null))
	}

	fun uploadDiaryImages(param: DiaryImagesUploadParam): FileUploadListResponse {
		val newFileNames: List<String> = generateDiaryImageNames(
			imageNames = param.images.map { it.originalFilename!! },
			userId = param.userId
		)

		return fileUploadService.uploadAll(FilesUploadRequest(param.images, newFileNames, param.domain, param.userId))
	}
}
