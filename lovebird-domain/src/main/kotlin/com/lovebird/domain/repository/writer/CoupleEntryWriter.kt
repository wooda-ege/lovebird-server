package com.lovebird.domain.repository.writer

import com.lovebird.domain.entity.CoupleEntry
import com.lovebird.domain.repository.jpa.CoupleJpaRepository
import org.springframework.stereotype.Component

@Component
class CoupleEntryWriter(
	private val coupleJpaRepository: CoupleJpaRepository
) {

	fun saveAll(coupleEntries: List<CoupleEntry>) {
		coupleJpaRepository.saveAll(coupleEntries)
	}
}
