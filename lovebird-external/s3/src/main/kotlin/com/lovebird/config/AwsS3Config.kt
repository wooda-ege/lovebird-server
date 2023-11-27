package com.lovebird.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AwsS3Config {

	@Value("\${cloud.aws.credentials.access-key}")
	private lateinit var accessKey: String

	@Value("\${cloud.aws.credentials.secret-key}")
	private lateinit var secretKey: String

	@Value("\${cloud.aws.region.static}")
	private lateinit var region: String

	@Value("\${cloud.aws.s3-bucket}")
	lateinit var bucketName: String

	@Bean
	fun amazonS3Client(): AmazonS3Client {
		val basicAWSCredentials = BasicAWSCredentials(accessKey, secretKey)

		return AmazonS3ClientBuilder.standard()
			.withRegion(region)
			.withCredentials(AWSStaticCredentialsProvider(basicAWSCredentials))
			.build() as AmazonS3Client
	}
}
