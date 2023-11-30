package com.lovebird.repository.reader

import com.lovebird.annotation.Reader
import com.lovebird.entity.User
import com.lovebird.repository.jpa.UserJpaRepository
import jakarta.persistence.EntityNotFoundException

@Reader
class UserReader(
	private val userJpaRepository: UserJpaRepository
) {

	fun findEntityById(id: Long): User {
		return userJpaRepository.findById(id).orElseThrow { EntityNotFoundException() }
	}
}
