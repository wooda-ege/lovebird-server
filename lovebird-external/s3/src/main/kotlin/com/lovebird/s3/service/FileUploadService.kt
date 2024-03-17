package com.lovebird.s3.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.lovebird.s3.dto.request.FileUploadRequest
import com.lovebird.s3.dto.request.FilesUploadRequest
import com.lovebird.s3.dto.response.FileUploadListResponse
import com.lovebird.s3.dto.response.FileUploadResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FileUploadService(
	private val s3Client: AmazonS3,
	@Value("\${cloud.aws.s3-bucket}")
	private val bucketName: String
) {

	fun uploadAll(dto: FilesUploadRequest): FileUploadListResponse {
		val fileUrls: MutableList<FileUploadResponse> = mutableListOf()

		dto.files.indices.forEach { idx ->
			val file: MultipartFile = dto.files[idx]
			val fileName: String = getPath(dto.userId, dto.domain, dto.fileNames[idx])

			fileUrls.add(FileUploadResponse(fileName, putS3(file, fileName)))
		}

		return FileUploadListResponse.from(fileUrls)
	}

	fun upload(dto: FileUploadRequest): FileUploadResponse {
		val fileName: String = getPath(dto.userId, dto.domain, dto.fileName)

		return FileUploadResponse(fileName, putS3(dto.file, fileName))
	}

	private fun putS3(file: MultipartFile, fileName: String): String {
		s3Client.putObject(
			PutObjectRequest(bucketName, fileName, file.inputStream, getObjectMetadata(file))
				.withCannedAcl(CannedAccessControlList.PublicRead)
		)

		return getS3Url(fileName)
	}

	private fun getS3Url(fileName: String): String {
		return s3Client.getUrl(bucketName, fileName).toString()
	}

	private fun getObjectMetadata(multipartFile: MultipartFile): ObjectMetadata {
		val objectMetaData = ObjectMetadata()
		objectMetaData.contentType = multipartFile.contentType
		objectMetaData.contentLength = multipartFile.size

		return objectMetaData
	}

	private fun getPath(userId: Long?, domain: String, filename: String): String {
		return if (userId == null) {
			"users/%s/%s".format(domain, filename)
		} else {
			"users/%s/%n/%s".format(domain, userId, filename)
		}
	}
}
