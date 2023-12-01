package com.lovebird.domain.repository.reader

import com.lovebird.domain.entity.CoupleCode
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.jpa.CoupleCodeJpaRepository
import org.springframework.stereotype.Component

@Component
class CoupleCodeReader(
	private val coupleCodeJpaRepository: CoupleCodeJpaRepository
) {

	fun findByUser(user: User): CoupleCode? {
		return coupleCodeJpaRepository.findByUser(user)
	}

	fun existsByCode(code: String): Boolean {
		return coupleCodeJpaRepository.existsByCode(code)
	}
}
