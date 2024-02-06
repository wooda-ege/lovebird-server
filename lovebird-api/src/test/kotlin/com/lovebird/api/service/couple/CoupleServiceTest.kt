package com.lovebird.api.service.couple

import com.lovebird.api.common.base.ServiceDescribeSpec
import com.lovebird.common.enums.Provider
import com.lovebird.common.exception.LbException
import com.lovebird.domain.entity.CoupleEntry
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.reader.CoupleEntryReader
import com.lovebird.domain.repository.writer.CoupleEntryWriter
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk

class CoupleServiceTest : ServiceDescribeSpec({

	val coupleEntryReader: CoupleEntryReader = mockk<CoupleEntryReader>(relaxed = true)
	val coupleEntryWriter: CoupleEntryWriter = mockk<CoupleEntryWriter>(relaxed = true)
	val coupleService = CoupleService(coupleEntryReader, coupleEntryWriter)

	afterEach {
		clearMocks(coupleEntryReader, coupleEntryWriter)
	}

	describe("existByUser()") {
		val user: User = getUser("123456789")

		context("해당 유저가 커플일 때") {
			every { coupleEntryReader.existsByUser(user) } returns true
			it("True를 반환한다") {
				coupleService.existByUser(user) shouldBe true
			}
		}

		context("해당 유저가 커플이 아닐 때") {
			every { coupleEntryReader.existsByUser(user) } returns false
			it("False를 반환한다") {
				coupleService.existByUser(user) shouldBe false
			}
		}
	}

	describe("findPartnerByUser()") {
		val user: User = getUser("123456789")
		val partner: User = getUser("987654321")
		val coupleEntry: CoupleEntry = getCoupleEntry(user, partner)

		context("해당 유저가 커플일 때") {
			every { coupleEntryReader.findByUser(user) } returns coupleEntry
			it("커플의 상대방을 반환한다") {
				coupleService.findPartnerByUser(user) shouldBe partner
			}
		}

		context("해당 유저가 커플이 아닐 때") {
			every { coupleEntryReader.findByUser(user) } returns null
			it("Null을 반환한다") {
				coupleService.findPartnerByUser(user) shouldBe null
			}
		}
	}

	describe("validateCouple()") {
		val user1 = getUser("123456789")
		val user2 = getUser("987654321")

		context("user1과 user2가 다른 유저이고, 둘다 커플이 아닌 경우") {
			every { coupleEntryReader.existsByUser(user1) } returns false
			every { coupleEntryReader.existsByUser(user2) } returns false
			it("예외를 반환하지 않는다") {
				shouldNotThrow<LbException> { coupleService.validateCouple(user1, user2) }
			}
		}

		context("user1이 이미 커플인 경우") {
			every { coupleEntryReader.existsByUser(user1) } returns true
			every { coupleEntryReader.existsByUser(user2) } returns false
			it("예외를 반환한다") {
				shouldThrow<LbException> { coupleService.validateCouple(user1, user2) }
			}
		}

		context("user2가 이미 커플인 경우") {
			every { coupleEntryReader.existsByUser(user1) } returns false
			every { coupleEntryReader.existsByUser(user2) } returns true
			it("예외를 반환한다") {
				shouldThrow<LbException> { coupleService.validateCouple(user1, user2) }
			}
		}

		context("user1과 user2가 동일한 유저인 경우 경우") {
			every { coupleEntryReader.existsByUser(user1) } returns false
			every { coupleEntryReader.existsByUser(user2) } returns false
			it("예외를 반환한다") {
				shouldThrow<LbException> { coupleService.validateCouple(user1, user1) }
			}
		}
	}

	describe("saveAll()") {
		val user1 = getUser("123456789")
		val user2 = getUser("987654321")

		context("정상적인 파라미터일 경우") {
			it("커플을 저장한다") {
				shouldNotThrow<LbException> { coupleService.saveAll(user1, user2) }
			}
		}
	}

	describe("saveAllWithValidation()") {
		val user1 = getUser("123456789")
		val user2 = getUser("987654321")

		context("user1과 user2가 다른 유저이고, 둘다 커플이 아닌 경우") {
			every { coupleEntryReader.existsByUser(user1) } returns false
			every { coupleEntryReader.existsByUser(user2) } returns false
			it("예외를 반환하지 않는다") {
				shouldNotThrow<LbException> { coupleService.saveAllWithValidation(user1, user2) }
			}
		}

		context("user1이 이미 커플인 경우") {
			every { coupleEntryReader.existsByUser(user1) } returns true
			every { coupleEntryReader.existsByUser(user2) } returns false
			it("예외를 반환한다") {
				shouldThrow<LbException> { coupleService.saveAllWithValidation(user1, user2) }
			}
		}

		context("user2가 이미 커플인 경우") {
			every { coupleEntryReader.existsByUser(user1) } returns false
			every { coupleEntryReader.existsByUser(user2) } returns true
			it("예외를 반환한다") {
				shouldThrow<LbException> { coupleService.saveAllWithValidation(user1, user2) }
			}
		}

		context("user1과 user2가 동일한 유저인 경우 경우") {
			every { coupleEntryReader.existsByUser(user1) } returns false
			every { coupleEntryReader.existsByUser(user2) } returns false
			it("예외를 반환한다") {
				shouldThrow<LbException> { coupleService.saveAllWithValidation(user1, user1) }
			}
		}
	}
}) {
	companion object {

		fun getCoupleEntry(user: User, partner: User): CoupleEntry {
			return CoupleEntry(
				user = user,
				partner = partner
			)
		}

		fun getUser(providerId: String): User {
			return User(
				provider = Provider.APPLE,
				providerId = providerId,
				deviceToken = "test-token"
			)
		}
	}
}
