package com.lovebird.domain.repository.reader

import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.lovebird.domain.annotation.Reader
import com.lovebird.domain.dto.query.ProfilePartnerResponseParam
import com.lovebird.domain.dto.query.ProfileUserResponseParam
import com.lovebird.domain.entity.Profile
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.jpa.ProfileJpaRepository
import com.lovebird.domain.repository.query.ProfileQueryRepository

@Reader
class ProfileReader(
	private val profileJpaRepository: ProfileJpaRepository,
	private val profileQueryRepository: ProfileQueryRepository
) {

	fun findEntityByUser(user: User): Profile {
		return profileJpaRepository.findByUser(user) ?: throw LbException(ReturnCode.NOT_EXIST_PROFILE)
	}

	fun findUserProfileByUser(user: User): ProfileUserResponseParam {
		return profileQueryRepository.findDetailUserParamByUser(user.id!!) ?: throw LbException(ReturnCode.NOT_EXIST_PROFILE)
	}

	fun findPartnerProfileByUser(partnerId: Long): ProfilePartnerResponseParam? {
		return profileQueryRepository.findDetailPartnerParamByUser(partnerId)
	}
}
