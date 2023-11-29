package com.lovebird.provider

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.lovebird.config.AwsS3Config
import org.springframework.stereotype.Component
import java.util.Date

@Component
class PresignedUrlProvider(
	private val awsS3Config: AwsS3Config
) {

	fun getUploadPresignedUrl(domain: String, userId: Long, filename: String): String {
		val path = getPath(userId, domain, filename)
		return getPresignedUrl(path, HttpMethod.PUT)
	}

	private fun getPresignedUrl(path: String, method: HttpMethod): String {
		return awsS3Config.amazonS3Client().generatePresignedUrl(
			getGeneratePresignedUrlRequest(path, method, getExpiration())
		).toString()
	}

	private fun getGeneratePresignedUrlRequest(
		path: String,
		method: HttpMethod,
		expiration: Date
	): GeneratePresignedUrlRequest {
		return GeneratePresignedUrlRequest(awsS3Config.getBucketName(), path, method).withExpiration(expiration)
	}

	private fun getExpiration(): Date {
		val expiration = Date()
		expiration.time += 180000
		return expiration
	}

	private fun getPath(userId: Long, domain: String, filename: String): String {
		return "users/%n/%s/%s".format(userId, domain, filename)
	}
}
