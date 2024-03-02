package com.lovebird.api.service.user

import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.reader.UserReader
import com.lovebird.domain.repository.writer.UserWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
	private val userReader: UserReader,
	private val userWriter: UserWriter
) {
	@Transactional
	fun save(user: User): User {
		return userWriter.save(user)
	}

	fun findById(id: Long): User {
		return userReader.findEntityById(id)
	}

	fun findByProviderId(providerId: String): User? {
		return userReader.findEntityByProviderId(providerId)
	}

	fun delete(user: User) {
		userWriter.delete(user)
	}
}
