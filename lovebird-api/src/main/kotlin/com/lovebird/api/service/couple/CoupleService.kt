package com.lovebird.api.service.couple

import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.lovebird.domain.entity.CoupleEntry
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.reader.CoupleEntryReader
import com.lovebird.domain.repository.writer.CoupleEntryWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CoupleService(
	private val coupleEntryReader: CoupleEntryReader,
	private val coupleEntryWriter: CoupleEntryWriter
) {

	@Transactional
	fun saveAllWithValidation(user1: User, user2: User) {
		validateCouple(user1, user2)
		saveAll(user1, user2)
	}

	@Transactional
	fun saveAll(user1: User, user2: User) {
		coupleEntryWriter.saveAll(listOf(CoupleEntry(user1, user2), CoupleEntry(user2, user1)))
	}

	@Transactional(readOnly = true)
	fun findPartnerByUser(user: User): User? {
		return coupleEntryReader.findByUser(user)?.partner
	}

	@Transactional(readOnly = true)
	fun existByUser(user: User): Boolean {
		return coupleEntryReader.existsByUser(user)
	}

	fun validateCouple(user1: User, user2: User) {
		if (existByUser(user1) || existByUser(user2)) {
			throw LbException(ReturnCode.ALREADY_EXIST_COUPLE)
		}

		if (user1 == user2) {
			throw LbException(ReturnCode.CAN_NOT_LINK_SELF)
		}
	}

	fun deleteByUser(user: User) {
		coupleEntryWriter.deleteByUser(user)
		coupleEntryWriter.deleteByPartner(user)
	}
}
