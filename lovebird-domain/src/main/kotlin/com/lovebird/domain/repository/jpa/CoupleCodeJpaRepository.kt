package com.lovebird.domain.repository.jpa

import com.lovebird.domain.entity.CoupleCode
import com.lovebird.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CoupleCodeJpaRepository : JpaRepository<CoupleCode, Long> {

	fun existsByCode(code: String): Boolean
	fun findByUser(user: User): CoupleCode?
}
