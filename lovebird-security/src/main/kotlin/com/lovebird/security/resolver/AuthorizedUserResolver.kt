package com.lovebird.security.resolver

import com.lovebird.domain.entity.User
import com.lovebird.security.annotation.AuthorizedUser
import com.lovebird.security.vo.PrincipalUser
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class AuthorizedUserResolver : HandlerMethodArgumentResolver {
	override fun supportsParameter(parameter: MethodParameter): Boolean {
		return parameter.hasParameterAnnotation(AuthorizedUser::class.java) &&
			User::class.java.isAssignableFrom(parameter.parameterType)
	}

	override fun resolveArgument(
		parameter: MethodParameter,
		mavContainer: ModelAndViewContainer?,
		webRequest: NativeWebRequest,
		binderFactory: WebDataBinderFactory?
	): Any {
		val principalUser = SecurityContextHolder.getContext().authentication.principal as PrincipalUser

		return principalUser.getUser()
	}
}
