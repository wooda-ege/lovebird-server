package com.lovebird.domain.repository.writer

import com.lovebird.domain.annotation.Writer
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.jpa.UserJpaRepository

@Writer
class UserWriter(
	private val userJpaRepository: UserJpaRepository
) {

	fun save(user: User): User {
		return userJpaRepository.save(user)
	}
}
