package com.lovebird.api.dto.param.user

import com.lovebird.domain.entity.User
import com.lovebird.security.vo.PrincipalUser

data class UserAuthParam(
	val user: User,
	val principalUser: PrincipalUser
) {
	companion object {
		fun of(user: User): UserAuthParam {
			return UserAuthParam(user, PrincipalUser.of(user))
		}
	}
}
