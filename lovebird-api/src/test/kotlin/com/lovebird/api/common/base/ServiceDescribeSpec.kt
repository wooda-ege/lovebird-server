package com.lovebird.api.common.base

import com.lovebird.common.enums.Provider
import com.lovebird.domain.entity.User
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.test.util.ReflectionTestUtils

@ExtendWith(MockKExtension::class)
abstract class ServiceDescribeSpec(
	body: DescribeSpec.() -> Unit = {}
) : DescribeSpec(body) {

	companion object {
		fun <T> any(type: Class<T>): T = Mockito.any(type)

		fun getUser(id: Long): User {
			val user = User(
				provider = Provider.APPLE,
				providerId = "123456789",
				deviceToken = "test-token"
			)
			ReflectionTestUtils.setField(user, "id", id)

			return user
		}
	}
}
