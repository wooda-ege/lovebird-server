package com.lovebird.domain.repository.reader

import com.lovebird.domain.entity.Profile
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.jpa.ProfileJpaRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Component

@Component
class ProfileReader(
	private val profileJpaRepository: ProfileJpaRepository
) {

	fun findEntityByUser(user: User): Profile {
		return profileJpaRepository.findByUser(user) ?: throw EntityNotFoundException()
	}
}
