package com.lovebird.api.utils

import com.lovebird.domain.entity.CoupleEntry
import com.lovebird.domain.entity.User

object CoupleTestFixture {

	fun getCoupleEntry(): CoupleEntry {
		val user = CommonTestFixture.getUser(1L, "testProvideID1")
		val partner = CommonTestFixture.getUser(2L, "testProvideID2")

		return CoupleEntry(
			user = user,
			partner = partner
		)
	}

	fun getCoupleEntry(user: User, partner: User): CoupleEntry {
		return CoupleEntry(
			user = user,
			partner = partner
		)
	}
}
