package com.lovebird.api.utils

import com.lovebird.domain.dto.query.ProfilePartnerResponseParam
import com.lovebird.domain.dto.query.ProfileUserResponseParam
import java.time.LocalDate

object ProfileTestFixture {

	fun getProfileUserParam(): ProfileUserResponseParam {
		return ProfileUserResponseParam(
			userId = 1L,
			coupleEntryId = 1L,
			email = "test-email",
			nickname = "test-nickname",
			firstDate = LocalDate.of(2021, 1, 1),
			birthday = LocalDate.of(1998, 5, 6),
			profileImageUrl = "test-profile-image-url"
		)
	}

	fun getProfilePartnerParam(): ProfilePartnerResponseParam {
		return ProfilePartnerResponseParam(
			partnerId = 2L,
			partnerNickname = "test-partner-nickname",
			partnerBirthday = LocalDate.of(1998, 3, 16),
			partnerImageUrl = "test-partner-image-url"
		)
	}
}
