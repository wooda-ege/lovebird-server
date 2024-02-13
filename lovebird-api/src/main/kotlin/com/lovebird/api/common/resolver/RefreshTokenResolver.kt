package com.lovebird.api.common.resolver

import com.lovebird.api.annotation.RefreshToken
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class RefreshTokenResolver : HandlerMethodArgumentResolver {

	override fun supportsParameter(parameter: MethodParameter): Boolean {
		return parameter.hasParameterAnnotation(RefreshToken::class.java) && String::class.java.isAssignableFrom(parameter.parameterType)
	}

	override fun resolveArgument(
		parameter: MethodParameter,
		mavContainer: ModelAndViewContainer?,
		webRequest: NativeWebRequest,
		binderFactory: WebDataBinderFactory?
	): String? {
		return webRequest.getHeader("Refresh")
	}
}
