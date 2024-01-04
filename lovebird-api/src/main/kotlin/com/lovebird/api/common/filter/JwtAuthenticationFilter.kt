package com.lovebird.api.common.filter

import com.lovebird.api.validator.JwtValidator
import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
	private val jwtValidator: JwtValidator,
	@Value("\${jwt.access-header}")
	private val accessHeader: String,
	@Value("\${jwt.grant-type}")
	private val grantType: String
) : OncePerRequestFilter() {

	override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
		val token: String? = getTokensFromHeader(request, accessHeader)

		token?.let {
			val accessToken = replaceBearerToBlank(it)

			SecurityContextHolder.getContext().authentication = jwtValidator.getAuthentication(accessToken)
		}

		filterChain.doFilter(request, response)
	}

	private fun getTokensFromHeader(request: HttpServletRequest, header: String): String? {
		return request.getHeader(header)
	}

	private fun replaceBearerToBlank(token: String): String {
		return if (!token.startsWith("%s ".format(grantType))) {
			throw LbException(ReturnCode.NOT_EXIST_BEARER_SUFFIX)
		} else {
			token.replace("%s ".format(grantType), "")
		}
	}
}
