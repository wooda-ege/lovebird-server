package com.lovebird.domain.repository.writer

import com.lovebird.common.annotation.Writer
import com.lovebird.domain.dto.command.ProfileUpdateRequestParam
import com.lovebird.domain.entity.Profile
import com.lovebird.domain.repository.jpa.ProfileJpaRepository

@Writer
class ProfileWriter(
	private val profileJpaRepository: ProfileJpaRepository
) {

	fun save(profile: Profile) {
		profileJpaRepository.save(profile)
	}

	fun update(profile: Profile, param: ProfileUpdateRequestParam) {
		profile.update(param)
	}
}
