package com.lovebird.domain.repository.reader

import com.lovebird.common.annotation.Reader
import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.lovebird.domain.dto.query.ProfileDetailParam
import com.lovebird.domain.entity.Profile
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.jpa.ProfileJpaRepository
import com.lovebird.domain.repository.query.ProfileQueryRepository
import jakarta.persistence.EntityNotFoundException

@Reader
class ProfileReader(
	private val profileJpaRepository: ProfileJpaRepository,
	private val profileQueryRepository: ProfileQueryRepository
) {

	fun findEntityByUser(user: User): Profile {
		return profileJpaRepository.findByUser(user) ?: throw EntityNotFoundException()
	}

	fun findDetailParamByUser(user: User): ProfileDetailParam {
		return profileQueryRepository.findDetailParamByUser(user) ?: throw LbException(ReturnCode.NOT_EXIST_PROFILE)
	}
}
