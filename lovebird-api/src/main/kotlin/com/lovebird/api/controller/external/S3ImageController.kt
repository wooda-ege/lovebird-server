package com.lovebird.api.controller.external

import com.lovebird.api.annotation.AuthorizedUser
import com.lovebird.api.dto.param.external.DiaryImagesUploadParam
import com.lovebird.api.dto.param.external.ProfileImageUploadParam
import com.lovebird.api.service.external.S3ImageService
import com.lovebird.common.response.ApiResponse
import com.lovebird.domain.entity.User
import com.lovebird.s3.dto.response.FileUploadListResponse
import com.lovebird.s3.dto.response.FileUploadResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/images")
class S3ImageController(
	private val s3ImageService: S3ImageService
) {

	@PostMapping("/profile")
	fun uploadProfileImage(
		@RequestPart(value = "image") image: MultipartFile
	): ApiResponse<FileUploadResponse> {
		val fileUploadResponse = s3ImageService.uploadProfileImage(ProfileImageUploadParam.from(image))

		return ApiResponse.success(fileUploadResponse)
	}

	@PostMapping("/diary")
	fun uploadDiaryImages(
		@AuthorizedUser user: User,
		@RequestPart(value = "images") images: List<MultipartFile>
	): ApiResponse<FileUploadListResponse> {
		val fileUploadListResponse = s3ImageService.uploadDiaryImages(DiaryImagesUploadParam.of(images, user.id!!))

		return ApiResponse.success(fileUploadListResponse)
	}
}
