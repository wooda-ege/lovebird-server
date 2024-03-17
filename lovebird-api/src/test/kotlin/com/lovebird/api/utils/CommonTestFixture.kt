package com.lovebird.api.utils

import com.lovebird.api.vo.PrincipalUser
import com.lovebird.common.enums.Provider
import com.lovebird.domain.entity.User
import org.springframework.mock.web.MockMultipartFile
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

	fun getPrincipalUser(id: Long, providerId: String): PrincipalUser {
		val user = getUser(id, providerId)
		return PrincipalUser.from(user)
	}

	fun getMultipartFile(): MockMultipartFile {
		val path = "image.png"
		val contentType = "image/png"

		return MockMultipartFile("image", path, contentType, "image".toByteArray())
	}
}
