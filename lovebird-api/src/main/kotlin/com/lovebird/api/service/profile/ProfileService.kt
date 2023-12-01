package com.lovebird.api.service.profile

import com.lovebird.api.dto.param.profile.ProfileCreateParam
import com.lovebird.domain.repository.reader.ProfileReader
import com.lovebird.domain.repository.writer.ProfileWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileService(
	private val profileReader: ProfileReader,
	private val profileWriter: ProfileWriter
) {

	@Transactional
	fun save(param: ProfileCreateParam) {
		profileWriter.save(param.toEntity())
	}
}
