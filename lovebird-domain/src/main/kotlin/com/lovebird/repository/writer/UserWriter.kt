package com.lovebird.repository.writer

import com.lovebird.annotation.Writer
import com.lovebird.entity.User
import com.lovebird.repository.jpa.UserJpaRepository

@Writer
class UserWriter(
	private val userJpaRepository: UserJpaRepository
) {

	fun save(user: User) {
		userJpaRepository.save(user)
	}
}
