package com.lovebird.api.service.external

import com.lovebird.api.common.base.ServiceDescribeSpec
import com.lovebird.api.dto.param.external.DiaryUploadPresignedUrlParam
import com.lovebird.api.dto.param.external.ProfileUploadPresignedUrlParam
import com.lovebird.api.dto.response.external.PresignedUrlListResponse
import com.lovebird.api.dto.response.external.PresignedUrlResponse
import com.lovebird.common.enums.Domain
import com.lovebird.s3.provider.PresignedUrlProvider
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk

class PresignedUrlServiceTest : ServiceDescribeSpec({

	val presignedUrlProvider: PresignedUrlProvider = mockk<PresignedUrlProvider>(relaxed = true)

	val presignedUrlService = PresignedUrlService(presignedUrlProvider)

	afterEach {
		clearMocks(presignedUrlProvider)
	}

	describe("getProfilePresignedUrl()") {
		val providerId = "provider"
		val filename = "test.png"
		val domain = Domain.PROFILE
		val presignedUrl = "https://cdn.lovebird.com/profile/1-profile.png"
		val newFilename = "provider-profile.png"

		context("정상적인 Parameter가 주어졌을 때") {
			val param = ProfileUploadPresignedUrlParam(providerId, filename)

			every { presignedUrlProvider.getUploadPresignedUrl(domain.lower(), null, any()) } returns presignedUrl

			it("프로필 사진 업로드용 presigned url을 반환한다.") {
				val response: PresignedUrlResponse = presignedUrlService.getProfilePresignedUrl(param)

				response.presignedUrl shouldNotBe null
				response.filename shouldNotBe null
			}
		}
	}

	describe("getDiaryPresignedUrls()") {
		val userId = 1L
		val filenames = listOf("test1.png", "test2.png")
		val domain = Domain.DIARY
		val presignedUrl = "https://cdn.lovebird.com/profile/1_1-1.png"

		context("정상적인 Parameter가 주어졌을 때") {
			val param = DiaryUploadPresignedUrlParam(userId, filenames)

			every { presignedUrlProvider.getUploadPresignedUrl(domain.lower(), param.userId, any()) } returns presignedUrl

			it("다이어리 사진 업로드용 presigned url을 반환한다.") {
				val response: PresignedUrlListResponse = presignedUrlService.getDiaryPresignedUrls(param)

				response.totalCount shouldBe 2
			}
		}
	}
})
