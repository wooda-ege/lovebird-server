package com.lovebird.api.controller.external

import com.lovebird.api.common.base.ControllerDescribeSpec
import com.lovebird.api.service.external.S3ImageService
import com.lovebird.api.utils.CommonTestFixture.getMultipartFile
import com.lovebird.api.utils.restdocs.ARRAY
import com.lovebird.api.utils.restdocs.NUMBER
import com.lovebird.api.utils.restdocs.STRING
import com.lovebird.api.utils.restdocs.andDocument
import com.lovebird.api.utils.restdocs.headerMeans
import com.lovebird.api.utils.restdocs.requestHeaders
import com.lovebird.api.utils.restdocs.restDocMockMvcBuild
import com.lovebird.api.utils.restdocs.type
import com.lovebird.s3.dto.response.FileUploadListResponse
import com.lovebird.s3.dto.response.FileUploadResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext

@WebMvcTest(S3ImageController::class)
class S3ImageControllerTest(
	@MockkBean(relaxed = true)
	private val s3ImageService: S3ImageService,
	@Autowired
	private val context: WebApplicationContext
) : ControllerDescribeSpec({

	val baseUrl = "/api/v1/images"
	val restDocumentation = ManualRestDocumentation()
	val mockMvc = restDocMockMvcBuild(context, restDocumentation)

	beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
	afterEach { restDocumentation.afterTest() }

	describe("POST : /api/v1/images/profile") {
		val url = "$baseUrl/profile"

		context("유효한 요청이 전달되면") {
			val image = getMultipartFile()
			val response = FileUploadResponse("1-profile.png", "프로필 이미지 url")
			val request = multipart(url)
				.file(image)
				.contentType(MediaType.APPLICATION_JSON)

			it("1000 SUCCESS") {
				every { s3ImageService.uploadProfileImage(any()) } returns response

				mockMvc
					.perform(request)
					.andExpect(status().isOk)
					.andDocument(
						"1000-profile-image-upload",
						envelopeResponseBody(
							"data.fileName" type STRING means "새로운 파일 이름",
							"data.fileUrl" type STRING means "파일 URL"
						)
					)
			}
		}
	}

	describe("POST : /api/v1/images/diary") {
		val url = "$baseUrl/diary"

		context("유효한 요청이 전달되면") {
			val image = getMultipartFile()
			val response = FileUploadListResponse.from(
				listOf(
					FileUploadResponse("image1.png", "url1"),
					FileUploadResponse("image2.png", "url2")
				)
			)
			val request = multipart(url)
				.file(
					MockMultipartFile(
						"image",
						"",
						"multipart/form-data",
						image.inputStream
					)
				)
				.header(HttpHeaders.AUTHORIZATION, "Bearer access-token")

			it("1000 SUCCESS") {
				every { s3ImageService.uploadDiaryImages(any()) } returns response

				mockMvc
					.perform(request)
					.andExpect(status().isOk)
					.andDocument(
						"1000-diary-images-upload",
						requestHeaders(
							"Authorization" headerMeans "액세스 토큰"
						),
						envelopeResponseBody(
							"data.fileUrls" type ARRAY means "파일 Url 리스트",
							"data.fileUrls[].fileName" type STRING means "파일 이름",
							"data.fileUrls[].fileUrl" type STRING means "파일 URL",
							"data.totalCount" type NUMBER means "총 개수"
						)
					)
			}
		}
	}
})
