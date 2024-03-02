package com.lovebird.domain.repository.jpa

import com.lovebird.domain.entity.CoupleEntry
import com.lovebird.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CoupleEntryJpaRepository : JpaRepository<CoupleEntry, Long> {

	fun deleteByPartner(partner: User)

	fun deleteByUser(user: User)
}
