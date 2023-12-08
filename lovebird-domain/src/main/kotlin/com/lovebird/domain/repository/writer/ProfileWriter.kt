package com.lovebird.domain.repository.writer

import com.lovebird.common.annotation.Writer
import com.lovebird.domain.dto.command.ProfileUpdateParam
import com.lovebird.domain.entity.Profile
import com.lovebird.domain.repository.jpa.ProfileJpaRepository

@Writer
class ProfileWriter(
	private val profileJpaRepository: ProfileJpaRepository
) {

	fun save(profile: Profile) {
		profileJpaRepository.save(profile)
	}

	fun update(profile: Profile, param: ProfileUpdateParam) {
		profile.update(param)
	}
}
