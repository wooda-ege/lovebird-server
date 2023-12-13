package com.lovebird.domain.repository.writer

import com.lovebird.domain.annotation.Writer
import com.lovebird.domain.entity.CoupleEntry
import com.lovebird.domain.repository.jpa.CoupleEntryJpaRepository

@Writer
class CoupleEntryWriter(
	private val coupleEntryJpaRepository: CoupleEntryJpaRepository
) {

	fun saveAll(coupleEntries: List<CoupleEntry>) {
		coupleEntryJpaRepository.saveAll(coupleEntries)
	}
}
