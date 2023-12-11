package com.lovebird.domain.repository.reader

import com.lovebird.domain.annotation.Reader
import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.jpa.UserJpaRepository

@Reader
class UserReader(
	private val userJpaRepository: UserJpaRepository
) {

	fun findEntityById(id: Long): User {
		return userJpaRepository.findById(id).orElseThrow { LbException(ReturnCode.NOT_EXIST_USER) }
	}

	fun findEntityByProviderId(providerId: String): User? {
		return userJpaRepository.findByProviderId(providerId)
	}
}
