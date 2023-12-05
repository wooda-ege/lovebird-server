package com.lovebird.domain.repository.reader

import com.lovebird.common.annotation.Reader
import com.lovebird.domain.entity.CoupleEntry
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.query.CoupleEntryQueryRepository

@Reader
class CoupleEntryReader(
	private val coupleEntryQueryRepository: CoupleEntryQueryRepository
) {

	fun existsByUser(user: User): Boolean {
		return findByUser(user) != null
	}

	fun findByUser(user: User): CoupleEntry? {
		return coupleEntryQueryRepository.findByUser(user)
	}
}
