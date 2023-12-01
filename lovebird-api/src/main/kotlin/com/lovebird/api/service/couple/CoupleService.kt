package com.lovebird.api.service.couple

import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.reader.CoupleReader
import org.springframework.stereotype.Service

@Service
class CoupleService(
	private val coupleReader: CoupleReader
) {

	fun existByUser(user: User): Boolean {
		return coupleReader.existsByUser(user)
	}
}
