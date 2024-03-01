package com.lovebird.api.service.profile

import com.lovebird.api.common.base.ServiceDescribeSpec
import com.lovebird.api.dto.param.profile.ProfileCreateParam
import com.lovebird.api.dto.response.profile.ProfileDetailResponse
import com.lovebird.api.utils.ProfileTestFixture.getProfilePartnerParam
import com.lovebird.api.utils.ProfileTestFixture.getProfileUserParam
import com.lovebird.common.enums.Gender
import com.lovebird.common.enums.Provider
import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.lovebird.domain.dto.command.ProfileUpdateRequestParam
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.reader.CoupleEntryReader
import com.lovebird.domain.repository.reader.ProfileReader
import com.lovebird.domain.repository.writer.ProfileWriter
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate

class ProfileServiceTest : ServiceDescribeSpec({

	val profileReader: ProfileReader = mockk<ProfileReader>(relaxed = true)
	val profileWriter: ProfileWriter = mockk<ProfileWriter>(relaxed = true)
	val coupleEntryReader: CoupleEntryReader = mockk<CoupleEntryReader>(relaxed = true)
	val profileService = ProfileService(profileReader, profileWriter, coupleEntryReader)

	afterEach {
		clearMocks(profileReader, profileWriter)
	}

	describe("save()") {
		context("정상적인 param이 주어졌을 때") {
			val param = getCreateParam(LocalDate.of(1998, 5, 6), LocalDate.of(2023, 12, 2))
			it("저장에 성공한다") {
				shouldNotThrow<RuntimeException> { profileService.save(param) }
			}
		}
		context("생일 데이터와 사귄 날짜 데이터가 없는 param이 주어졌을 때") {
			val param = getCreateParam(null, LocalDate.of(2023, 12, 2))
			it("저장에 성공한다") {
				shouldNotThrow<RuntimeException> { profileService.save(param) }
			}
		}
	}

	describe("update()") {
		val user: User = getUser()

		context("정상적인 param이 주어졌을 때") {
			val param = getUpdateParam(LocalDate.of(1998, 5, 6), LocalDate.of(2023, 12, 2))
			it("프로필 수정에 성공한다") {
				shouldNotThrow<RuntimeException> { profileService.update(user, param) }
			}
		}
		context("생일 데이터와 사귄 날짜 데이터가 없는 param이 주어졌을 때") {
			val param = getUpdateParam(null, LocalDate.of(2023, 12, 2))
			it("프로필 수정에 성공한다") {
				shouldNotThrow<RuntimeException> { profileService.update(user, param) }
			}
		}
		context("해당 유저가 프로필을 가지고 있지 않을 때") {
			every { profileReader.findEntityByUser(user) } throws LbException(ReturnCode.NOT_EXIST_PROFILE)

			it("예외를 반환한다") {
				shouldThrow<LbException> { profileService.update(user, getUpdateParam(null, null)) }
			}
		}
	}

	describe("findDetailByUser()") {
		val user: User = getUser()

		context("해당 유저가 프로필을 가지고 있을 때") {
			val userParam = getProfileUserParam()
			val partnerParam = getProfilePartnerParam()

			every { profileReader.findUserProfileByUser(user) } returns userParam
			every { coupleEntryReader.findPartnerIdById(any()) } returns 2L
			every { profileReader.findPartnerProfileByUser(2L) } returns partnerParam

			it("상세 조회를 성공한다.") {
				val detailResponse: ProfileDetailResponse = profileService.findDetailByUser(user)

				detailResponse shouldBe ProfileDetailResponse.of(userParam, partnerParam)
			}
		}

		context("해당 유저가 프로필을 가지고 있지 않을 때") {
			every { profileReader.findUserProfileByUser(user) } throws LbException(ReturnCode.NOT_EXIST_PROFILE)

			it("예외를 반환한다") {
				shouldThrow<LbException> { profileService.findDetailByUser(user) }
			}
		}
	}
}) {

	companion object {
		fun getCreateParam(birthday: LocalDate?, firstDate: LocalDate?): ProfileCreateParam {
			return ProfileCreateParam(
				user = getUser(),
				imageUrl = "test-image-url",
				email = "test-email",
				nickname = "test-nickname",
				birthday = birthday,
				firstDate = firstDate,
				gender = Gender.MALE
			)
		}

		fun getUpdateParam(birthday: LocalDate?, firstDate: LocalDate?): ProfileUpdateRequestParam {
			return ProfileUpdateRequestParam(
				imageUrl = "test-image-url",
				email = "test-email",
				nickname = "test-nickname",
				birthday = birthday,
				firstDate = firstDate,
				gender = Gender.MALE
			)
		}

		fun getUser(): User {
			return User(
				provider = Provider.APPLE,
				providerId = "123456789",
				deviceToken = "test-token"
			)
		}
	}
}
