package com.lovebird.api.utils

import com.lovebird.common.enums.Provider
import com.lovebird.domain.entity.User
import org.springframework.test.util.ReflectionTestUtils

object CommonTestFixture {

	fun getUser(id: Long, providerId: String): User {
		val user = User(
			provider = Provider.APPLE,
			providerId = providerId,
			deviceToken = "test-token"
		)
		ReflectionTestUtils.setField(user, "id", id)
		return user
	}
}
