package com.lovebird.api.service.profile

import com.lovebird.api.dto.param.profile.ProfileCreateParam
import com.lovebird.api.dto.response.profile.ProfileDetailResponse
import com.lovebird.domain.dto.command.ProfileUpdateParam
import com.lovebird.domain.entity.Profile
import com.lovebird.domain.entity.User
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

	@Transactional
	fun update(user: User, param: ProfileUpdateParam) {
		profileWriter.update(findByUser(user), param)
	}

	@Transactional(readOnly = true)
	fun findDetailByUser(user: User): ProfileDetailResponse {
		return ProfileDetailResponse.of(profileReader.findDetailParamByUser(user))
	}

	private fun findByUser(user: User): Profile {
		return profileReader.findEntityByUser(user)
	}
}
