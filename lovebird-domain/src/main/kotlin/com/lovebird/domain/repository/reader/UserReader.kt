package com.lovebird.domain.repository.reader

import com.lovebird.common.annotation.Reader
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.jpa.UserJpaRepository
import jakarta.persistence.EntityNotFoundException

@Reader
class UserReader(
	private val userJpaRepository: UserJpaRepository
) {

	fun findEntityById(id: Long): User {
		return userJpaRepository.findById(id).orElseThrow { EntityNotFoundException() }
	}
}
