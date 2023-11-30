package com.lovebird.security.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.lovebird.common.exception.LbException
import com.lovebird.common.response.ApiResponse
import com.lovebird.enums.ReturnCode
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtExceptionFilter : OncePerRequestFilter() {

	override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
		try {
			filterChain.doFilter(request, response)
		} catch (e: LbException) {
			setErrorResponse(e.getReturnCode(), response)
		}
	}

	fun setErrorResponse(returnCode: ReturnCode, response: HttpServletResponse) {
		response.status = HttpStatus.UNAUTHORIZED.value()
		response.contentType = "application/json; charset=UTF-8"

		val result: ApiResponse<String> = ApiResponse.fail(returnCode)
		response.writer.write(toJson(result))
	}

	private fun toJson(data: Any): String {
		return ObjectMapper().writeValueAsString(data)
	}
}
