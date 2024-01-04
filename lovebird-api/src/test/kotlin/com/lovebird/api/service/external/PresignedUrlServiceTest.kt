package com.lovebird.api.service.external

import com.lovebird.api.common.base.ServiceDescribeSpec
import com.lovebird.api.dto.param.external.DiaryUploadPresignedUrlParam
import com.lovebird.api.dto.param.external.ProfileUploadPresignedUrlParam
import com.lovebird.api.dto.response.external.PresignedUrlListResponse
import com.lovebird.api.dto.response.external.PresignedUrlResponse
import com.lovebird.api.provider.FilenameProvider
import com.lovebird.common.enums.Domain
import com.lovebird.s3.provider.PresignedUrlProvider
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk

class PresignedUrlServiceTest : ServiceDescribeSpec({

	val presignedUrlProvider: PresignedUrlProvider = mockk<PresignedUrlProvider>(relaxed = true)
	val fileNameProvider = FilenameProvider()

	val presignedUrlService = PresignedUrlService(presignedUrlProvider, fileNameProvider)

	afterEach {
		clearMocks(presignedUrlProvider)
	}

	describe("getProfilePresignedUrl()") {
		val userId = 1L
		val filename = "test.png"
		val domain = Domain.PROFILE
		val presignedUrl = "https://cdn.lovebird.com/profile/1-profile.png"
		val newFilename = "1-profile.png"

		context("정상적인 Parameter가 주어졌을 때") {
			val param = ProfileUploadPresignedUrlParam(userId, filename)

			every { presignedUrlProvider.getUploadPresignedUrl(domain.lower(), param.userId, newFilename) } returns presignedUrl

			it("프로필 사진 업로드용 presigned url을 반환한다.") {
				val response: PresignedUrlResponse = presignedUrlService.getProfilePresignedUrl(param)

				response.presignedUrl shouldBe presignedUrl
				response.filename shouldBe newFilename
			}
		}
	}

	describe("getDiaryPresignedUrls()") {
		val userId = 1L
		val diaryId = 1L
		val filenames = listOf("test1.png", "test2.png")
		val domain = Domain.DIARY
		val presignedUrl1 = "https://cdn.lovebird.com/profile/1_1-1.png"
		val presignedUrl2 = "https://cdn.lovebird.com/profile/1_1-2.png"
		val newFilename1 = "1_1-1.png"
		val newFilename2 = "1_1-2.png"

		context("정상적인 Parameter가 주어졌을 때") {
			val param = DiaryUploadPresignedUrlParam(userId, diaryId, filenames)

			every { presignedUrlProvider.getUploadPresignedUrl(domain.lower(), param.userId, newFilename1) } returns presignedUrl1
			every { presignedUrlProvider.getUploadPresignedUrl(domain.lower(), param.userId, newFilename2) } returns presignedUrl2

			it("다이어리 사진 업로드용 presigned url을 반환한다.") {
				val response: PresignedUrlListResponse = presignedUrlService.getDiaryPresignedUrls(param)

				response.presignedUrls[0].presignedUrl shouldBe presignedUrl1
				response.presignedUrls[0].filename shouldBe newFilename1
				response.presignedUrls[1].presignedUrl shouldBe presignedUrl2
				response.presignedUrls[1].filename shouldBe newFilename2
				response.totalCount shouldBe 2
			}
		}
	}
})
