package com.lovebird.api.dto.param.user

import com.lovebird.api.vo.PrincipalUser
import com.lovebird.domain.entity.User

data class UserAuthParam(
	val user: User,
	val principalUser: PrincipalUser
) {
	companion object {
		fun from(user: User): UserAuthParam {
			return UserAuthParam(user, PrincipalUser.from(user))
		}
	}
}
