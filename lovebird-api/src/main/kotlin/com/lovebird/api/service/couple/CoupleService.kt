package com.lovebird.api.service.couple

import com.lovebird.domain.entity.CoupleEntry
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.reader.CoupleEntryReader
import com.lovebird.domain.repository.writer.CoupleEntryWriter
import org.springframework.stereotype.Service

@Service
class CoupleService(
	private val coupleEntryReader: CoupleEntryReader,
	private val coupleEntryWriter: CoupleEntryWriter
) {

	fun saveAll(user1: User, user2: User) {
		coupleEntryWriter.saveAll(
			listOf(
				CoupleEntry(user1, user2),
				CoupleEntry(user2, user1)
			)
		)
	}

	fun existByUser(user: User): Boolean {
		return coupleEntryReader.existsByUser(user)
	}
}
