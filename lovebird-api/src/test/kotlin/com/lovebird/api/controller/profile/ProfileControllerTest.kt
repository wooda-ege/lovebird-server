package com.lovebird.api.controller.profile

import com.lovebird.api.common.base.ControllerDescribeSpec
import com.lovebird.api.dto.request.profile.AnniversaryResponse
import com.lovebird.api.dto.request.profile.ProfileUpdateRequest
import com.lovebird.api.dto.response.profile.ProfileDetailResponse
import com.lovebird.api.service.profile.ProfileService
import com.lovebird.api.utils.andExpectData
import com.lovebird.api.utils.restdocs.NUMBER
import com.lovebird.api.utils.restdocs.OBJECT
import com.lovebird.api.utils.restdocs.RestDocsField
import com.lovebird.api.utils.restdocs.STRING
import com.lovebird.api.utils.restdocs.andDocument
import com.lovebird.api.utils.restdocs.headerMeans
import com.lovebird.api.utils.restdocs.requestBody
import com.lovebird.api.utils.restdocs.requestHeaders
import com.lovebird.api.utils.restdocs.restDocMockMvcBuild
import com.lovebird.api.utils.restdocs.type
import com.lovebird.api.utils.shouldBe
import com.lovebird.common.enums.AnniversaryType
import com.lovebird.common.enums.Gender
import com.lovebird.common.enums.ReturnCode
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.request
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext
import java.time.LocalDate

@WebMvcTest(ProfileController::class)
class ProfileControllerTest(
	@MockkBean(relaxed = true)
	private val profileService: ProfileService,
	@Autowired
	private val context: WebApplicationContext
) : ControllerDescribeSpec({

	val baseUrl = "/api/v1/profile"
	val restDocumentation = ManualRestDocumentation()
	val mockMvc = restDocMockMvcBuild(context, restDocumentation)

	beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
	afterEach { restDocumentation.afterTest() }

	describe("GET : /api/v1/profile") {

		context("유효한 요청이 전달 되면") {
			val response = getProfileDetailResponse()
			val request = request(HttpMethod.GET, baseUrl)
				.header(AUTHORIZATION, "Bearer access-token")

			it("1000 SUCCESS") {
				every { profileService.findDetailByUser(any()) } returns response
				mockMvc
					.perform(request)
					.andExpect(status().isOk)
					.andExpectData(
						jsonPath("$.code") shouldBe ReturnCode.SUCCESS.code,
						jsonPath("$.message") shouldBe ReturnCode.SUCCESS.message,
						jsonPath("$.data.userId") shouldBe response.userId,
						jsonPath("$.data.partnerId") shouldBe response.partnerId!!,
						jsonPath("$.data.email") shouldBe response.email,
						jsonPath("$.data.nickname") shouldBe response.nickname,
						jsonPath("$.data.partnerNickname") shouldBe response.partnerNickname!!,
						jsonPath("$.data.firstDate") shouldBe response.firstDate!!.toString(),
						jsonPath("$.data.birthday") shouldBe response.birthday!!.toString(),
						jsonPath("$.data.partnerBirthday") shouldBe response.partnerBirthday!!.toString(),
						jsonPath("$.data.dayCount") shouldBe response.dayCount!!,
						jsonPath("$.data.nextAnniversary.kind") shouldBe response.nextAnniversary!!.kind.toString(),
						jsonPath("$.data.nextAnniversary.seq") shouldBe response.nextAnniversary!!.seq.toString(),
						jsonPath("$.data.nextAnniversary.anniversaryDate") shouldBe response.nextAnniversary!!.anniversaryDate.toString(),
						jsonPath("$.data.profileImageUrl") shouldBe response.profileImageUrl!!,
						jsonPath("$.data.partnerImageUrl") shouldBe response.partnerImageUrl!!
					)
					.andDocument(
						"1000-profile-url",
						requestHeaders(
							"Authorization" headerMeans "액세스 토큰"
						),
						envelopeResponseBody().andWithPrefix("data.", getProfileDetailResponseSnippet())
					)
			}
		}
	}

	describe("PUT : /api/v1/profile") {

		context("유효한 요청이 전달 되면") {
			val requestBody = getProfileUpdateRequest()
			val requestJson = toJson(requestBody)
			val request = request(HttpMethod.PUT, baseUrl)
				.header(AUTHORIZATION, "Bearer access-token")
				.content(requestJson)
				.contentType(APPLICATION_JSON)

			it("1000 SUCCESS") {
				every { profileService.update(any(), requestBody.toParam()) } returns Unit

				mockMvc
					.perform(request)
					.andExpect(status().isOk)
					.andExpectData(
						jsonPath("$.code") shouldBe ReturnCode.SUCCESS.code,
						jsonPath("$.message") shouldBe ReturnCode.SUCCESS.message
					)
					.andDocument(
						"1000-update-profile-url",
						requestHeaders(
							"Authorization" headerMeans "액세스 토큰"
						),
						requestBody(
							"imageUrl" type STRING means "수정할 이미지 URL" isOptional true,
							"email" type STRING means "수정할 이메일" isOptional true,
							"nickname" type STRING means "수정할 닉네임" isOptional true,
							"birthday" type STRING means "수정할 생년월일" isOptional true,
							"firstDate" type STRING means "수정할 사귄 날짜" isOptional true,
							"gender" type STRING means "수정할 성별" isOptional true
						),
						envelopeResponseBody(dataOptional = true)
					)
			}
		}
	}
}) {
	companion object {
		fun getProfileDetailResponse(): ProfileDetailResponse {
			return ProfileDetailResponse(
				userId = 1L,
				partnerId = 2L,
				email = "",
				nickname = "test",
				partnerNickname = "test2",
				firstDate = LocalDate.of(2024, 1, 1),
				birthday = LocalDate.now(),
				partnerBirthday = LocalDate.now(),
				dayCount = 48,
				nextAnniversary = AnniversaryResponse(AnniversaryType.DAY, 3, LocalDate.of(2024, 1, 1).plusDays(300)),
				profileImageUrl = "profile image url",
				partnerImageUrl = "partner profile image url"
			)
		}

		fun getProfileUpdateRequest(): ProfileUpdateRequest {
			return ProfileUpdateRequest(
				imageUrl = "test-image-url",
				email = "test-email",
				nickname = "test-nickname",
				birthday = LocalDate.now(),
				firstDate = LocalDate.now(),
				gender = Gender.UNKNOWN
			)
		}

		fun getProfileDetailResponseSnippet(): List<FieldDescriptor> {
			return responseProfileDetailResponse(
				"userId" type NUMBER means "유저 아이디",
				"partnerId" type NUMBER means "파트너 아이디" isOptional true,
				"email" type STRING means "이메일",
				"nickname" type STRING means "닉네임",
				"partnerNickname" type STRING means "파트너 닉네임" isOptional true,
				"firstDate" type STRING means "사귄 날짜" isOptional true,
				"birthday" type STRING means "생년월일" isOptional true,
				"partnerBirthday" type STRING means "파트너 생년월일" isOptional true,
				"dayCount" type NUMBER means "사귄 일수" isOptional true,
				"nextAnniversary" type OBJECT means "다음 기념일" isOptional true,
				"nextAnniversary.kind" type STRING means "다음 기념일 종류" isOptional true,
				"nextAnniversary.seq" type NUMBER means "다음 기념일 순서" isOptional true,
				"nextAnniversary.anniversaryDate" type STRING means "다음 기념일 날짜" isOptional true,
				"profileImageUrl" type STRING means "프로필 이미지 URL" isOptional true,
				"partnerImageUrl" type STRING means "파트너 프로필 이미지 URL" isOptional true
			)
		}

		private fun responseProfileDetailResponse(vararg fields: RestDocsField): List<FieldDescriptor> {
			return fields.map { it.descriptor }
		}
	}
}
