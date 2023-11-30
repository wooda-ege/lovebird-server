package com.lovebird.domain.repository.writer

import com.lovebird.common.annotation.Writer
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.jpa.UserJpaRepository

@Writer
class UserWriter(
	private val userJpaRepository: UserJpaRepository
) {

	fun save(user: User) {
		userJpaRepository.save(user)
	}
}
