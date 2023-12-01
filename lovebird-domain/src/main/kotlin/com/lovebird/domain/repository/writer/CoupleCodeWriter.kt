package com.lovebird.domain.repository.writer

import com.lovebird.domain.entity.CoupleCode
import com.lovebird.domain.repository.jpa.CoupleCodeJpaRepository
import org.springframework.stereotype.Component

@Component
class CoupleCodeWriter(
	private val coupleCodeJpaRepository: CoupleCodeJpaRepository
) {

	fun save(coupleCode: CoupleCode): CoupleCode {
		return coupleCodeJpaRepository.save(coupleCode)
	}

	fun delete(coupleCode: CoupleCode) {
		coupleCodeJpaRepository.delete(coupleCode)
	}
}
