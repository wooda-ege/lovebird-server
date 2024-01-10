package com.lovebird.api.service.user

import com.lovebird.api.common.base.ServiceDescribeSpec
import com.lovebird.common.enums.Provider
import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.reader.UserReader
import com.lovebird.domain.repository.writer.UserWriter
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk

class UserServiceTest : ServiceDescribeSpec({

	val userReader: UserReader = mockk<UserReader>(relaxed = true)
	val userWriter: UserWriter = mockk<UserWriter>(relaxed = true)
	val userService = UserService(userReader, userWriter)

	afterEach {
		clearMocks(userReader, userWriter)
	}

	describe("save()") {
		context("정상적인 User가 주어졌을 때") {
			val user = User(
				provider = Provider.NAVER,
				providerId = "123456789",
				deviceToken = "test-token"
			)
			it("저장에 성공한다") {
				every { userWriter.save(user) } returns user

				val savedUser: User = userService.save(user)
				savedUser shouldBe user
			}
		}
		context("Device Token이 없는 User가 주어졌을 때") {
			val user = User(
				provider = Provider.NAVER,
				providerId = "123456789",
				deviceToken = null
			)
			it("저장에 성공한다") {
				every { userWriter.save(user) } returns user

				val savedUser: User = userService.save(user)
				savedUser shouldBe user
			}
		}
	}

	describe("findById()") {
		context("해당 id의 User가 존재할 때") {
			val user = User(
				provider = Provider.NAVER,
				providerId = "123456789",
				deviceToken = "test-token"
			)

			it("조회에 성공한다") {
				every { userReader.findEntityById(1L) } returns user

				val foundUser: User = userService.findById(1L)
				foundUser shouldBe user
			}
		}
		context("해당 id의 User가 존재하지 않을 때") {

			it("예외를 던진다") {
				every { userReader.findEntityById(1L) } throws LbException(ReturnCode.NOT_EXIST_USER)

				shouldThrow<LbException> { userService.findById(1L) }
			}
		}
	}

	describe("findByProviderId()") {
		context("해당 provider id의 User가 존재할 때") {
			val user = User(
				provider = Provider.NAVER,
				providerId = "123456789",
				deviceToken = "test-token"
			)

			it("조회에 성공한다") {
				every { userReader.findEntityByProviderId("123456789") } returns user

				val foundUser: User? = userService.findByProviderId("123456789")
				foundUser shouldBe user
			}
		}
		context("해당 provider id의 User가 존재하지 않을 때") {

			it("예외를 던진다") {
				every { userReader.findEntityByProviderId("123456789") } throws LbException(ReturnCode.NOT_EXIST_USER)

				shouldThrow<LbException> { userService.findByProviderId("123456789") }
			}
		}
	}
})
