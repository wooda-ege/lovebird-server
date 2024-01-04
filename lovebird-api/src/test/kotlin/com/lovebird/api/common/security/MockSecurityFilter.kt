package com.lovebird.api.common.security

import com.lovebird.api.vo.PrincipalUser
import com.lovebird.common.enums.Provider
import com.lovebird.domain.entity.User
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.FilterConfig
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import java.lang.reflect.Field

class MockSecurityFilter : Filter {
	override fun init(filterConfig: FilterConfig) {}

	override fun doFilter(
		request: ServletRequest,
		response: ServletResponse?,
		chain: FilterChain
	) {
		val principalUser = PrincipalUser(createMember(), mutableMapOf())
		SecurityContextHolder.getContext().authentication =
			UsernamePasswordAuthenticationToken(principalUser, "", principalUser.getAuthorities())

		chain.doFilter(request, response)
	}

	override fun destroy() {
		SecurityContextHolder.clearContext()
	}

	fun getFilters(mockHttpServletRequest: MockHttpServletRequest) {}

	companion object {
		private fun createMember(): User {
			val user = User(
				provider = Provider.NAVER,
				providerId = "123456789",
				deviceToken = "test-token"
			)

			val userClass: Class<User> = User::class.java
			try {
				val id: Field = userClass.getDeclaredField("id")
				id.isAccessible = true
				id.set(user, 1L)
			} catch (e: NoSuchFieldException) {
				throw RuntimeException(e)
			} catch (e: IllegalAccessException) {
				throw RuntimeException(e)
			}
			return user
		}
	}
}
