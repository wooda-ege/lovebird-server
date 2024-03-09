package com.lovebird.domain.repository.writer

import com.lovebird.domain.annotation.Writer
import com.lovebird.domain.entity.CoupleEntry
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.jpa.CoupleEntryJpaRepository

@Writer
class CoupleEntryWriter(
	private val coupleEntryJpaRepository: CoupleEntryJpaRepository
) {

	fun saveAll(coupleEntries: List<CoupleEntry>) {
		coupleEntryJpaRepository.saveAll(coupleEntries)
	}

	fun deleteByUser(user: User) {
		return coupleEntryJpaRepository.deleteByUser(user)
	}

	fun deleteByPartner(partner: User) {
		return coupleEntryJpaRepository.deleteByPartner(partner)
	}
}
