package com.lovebird.api.service.couple

import com.lovebird.api.common.base.ServiceDescribeSpec
import com.lovebird.api.dto.param.couple.CoupleLinkParam
import com.lovebird.common.enums.Provider
import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.lovebird.common.util.RandomUtils.generateCode
import com.lovebird.domain.entity.CoupleCode
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.reader.CoupleCodeReader
import com.lovebird.domain.repository.writer.CoupleCodeWriter
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.springframework.test.util.ReflectionTestUtils
import java.time.LocalDateTime

class CoupleCodeServiceTest : ServiceDescribeSpec({

	val coupleCodeWriter: CoupleCodeWriter = mockk<CoupleCodeWriter>(relaxed = true)
	val coupleCodeReader: CoupleCodeReader = mockk<CoupleCodeReader>(relaxed = true)
	val coupleService: CoupleService = mockk<CoupleService>(relaxed = true)
	val coupleCodeService = CoupleCodeService(coupleService, coupleCodeWriter, coupleCodeReader, "appleTestCode")

	afterEach {
		clearMocks(coupleCodeWriter, coupleCodeReader, coupleService)
	}

	describe("generateCodeIfNotExist()") {
		val user = getUser("123456789", 1L)

		context("해당 유저의 유효한 커플 코드가 존재할 때") {
			val coupleCode = getCoupleCode(user, generateCode())
			coupleCode.createdAt = LocalDateTime.now()

			it("커플 코드를 반환한다") {
				every { coupleCodeReader.findByUser(user) } returns coupleCode

				val response = coupleCodeService.generateCodeIfNotExist(user)
				response.coupleCode shouldBe coupleCode.code
			}
		}

		context("해당 유저의 유효하지 않은 커플 코드가 존재할 때") {
			val coupleCode = getCoupleCode(user, "ABCDEFGHI")
			coupleCode.createdAt = LocalDateTime.now().minusHours(25)

			val newCoupleCode = getCoupleCode(user, generateCode())
			newCoupleCode.createdAt = LocalDateTime.now()

			it("커플 코드를 재생성하여 반환한다") {
				every { coupleCodeReader.findByUser(user) } returns coupleCode
				every { coupleCodeReader.existsByCode(any()) } returns false
				every { coupleCodeWriter.save(any()) } returns newCoupleCode

				val response = coupleCodeService.generateCodeIfNotExist(user)
				response.coupleCode shouldBe newCoupleCode.code
			}
		}
	}

	describe("linkCouple()") {
		val user1 = getUser("123456789", 1L)
		val user2 = getUser("987654321", 2L)
		val coupleCode = getCoupleCode(user2, "ABCDEFGHI")
		val param = CoupleLinkParam(user1, coupleCode.code)

		context("유효한 커플 코드로 연동 요청을 했을 때") {
			coupleCode.createdAt = LocalDateTime.now()
			it("커플 연동에 성공한다") {
				every { coupleCodeReader.findByCode(coupleCode.code) } returns coupleCode

				val response = coupleCodeService.linkCouple(param)
				response.partnerId shouldBe user2.id
			}
		}

		context("커플 코드가 Apple 테스트 코드일 때") {
			val appleParam = CoupleLinkParam(user1, "appleTestCode")
			it("커플 연동에 성공한다") {
				val response = coupleCodeService.linkCouple(appleParam)
				response.partnerId shouldBe user1.id
			}
		}

		context("존재하지 않는 커플 코드로 연동 요청을 했을 때") {
			it("커플 연동에 실패한다") {
				every { coupleCodeReader.findByCode(coupleCode.code) } throws LbException(ReturnCode.WRONG_PARAMETER)

				shouldThrow<RuntimeException> { coupleCodeService.linkCouple(param) }
			}
		}

		context("만료된 커플 코드로 연동 요청을 했을 때") {
			coupleCode.createdAt = LocalDateTime.now().minusHours(25)
			it("커플 연동에 실패한다") {
				every { coupleCodeReader.findByCode(coupleCode.code) } returns coupleCode

				shouldThrow<RuntimeException> { coupleCodeService.linkCouple(param) }
			}
		}
	}
}) {
	companion object {

		fun getCoupleCode(user: User, code: String): CoupleCode {
			return CoupleCode(user, code)
		}

		fun getUser(providerId: String, id: Long): User {
			val user = User(
				provider = Provider.APPLE,
				providerId = providerId,
				deviceToken = "test-token"
			)
			ReflectionTestUtils.setField(user, "id", id)
			return user
		}
	}
}
