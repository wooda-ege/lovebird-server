package com.lovebird.domain.repository.reader

import com.lovebird.common.annotation.Reader
import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.lovebird.domain.entity.CoupleCode
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.jpa.CoupleCodeJpaRepository

@Reader
class CoupleCodeReader(
	private val coupleCodeJpaRepository: CoupleCodeJpaRepository
) {

	fun findByUser(user: User): CoupleCode? {
		return coupleCodeJpaRepository.findByUser(user)
	}

	fun findByCode(code: String): CoupleCode {
		return coupleCodeJpaRepository.findByCode(code) ?: throw LbException(ReturnCode.WRONG_PARAMETER)
	}

	fun existsByCode(code: String): Boolean {
		return coupleCodeJpaRepository.existsByCode(code)
	}
}
