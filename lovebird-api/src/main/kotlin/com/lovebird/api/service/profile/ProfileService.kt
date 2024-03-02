package com.lovebird.api.service.profile

import com.lovebird.api.dto.param.profile.ProfileCreateParam
import com.lovebird.api.dto.response.profile.ProfileDetailResponse
import com.lovebird.domain.dto.command.ProfileUpdateRequestParam
import com.lovebird.domain.dto.query.ProfilePartnerResponseParam
import com.lovebird.domain.dto.query.ProfileUserResponseParam
import com.lovebird.domain.entity.Profile
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.reader.CoupleEntryReader
import com.lovebird.domain.repository.reader.ProfileReader
import com.lovebird.domain.repository.writer.ProfileWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileService(
	private val profileReader: ProfileReader,
	private val profileWriter: ProfileWriter,
	private val coupleEntryReader: CoupleEntryReader
) {

	@Transactional
	fun save(param: ProfileCreateParam) {
		profileWriter.save(param.toEntity())
	}

	@Transactional
	fun update(user: User, param: ProfileUpdateRequestParam) {
		profileWriter.update(findByUser(user), param)
	}

	@Transactional(readOnly = true)
	fun findDetailByUser(user: User): ProfileDetailResponse {
		val userParam: ProfileUserResponseParam = profileReader.findUserProfileByUser(user)
		val partnerId: Long? = userParam.coupleEntryId?.let { coupleEntryReader.findPartnerIdById(it) }
		val partnerParam: ProfilePartnerResponseParam? = partnerId?.let { profileReader.findPartnerProfileByUser(it) }

		return ProfileDetailResponse.of(userParam, partnerParam)
	}

	private fun findByUser(user: User): Profile {
		return profileReader.findEntityByUser(user)
	}

	fun deleteByUser(user: User) {
		profileWriter.delete(user)
	}
}
