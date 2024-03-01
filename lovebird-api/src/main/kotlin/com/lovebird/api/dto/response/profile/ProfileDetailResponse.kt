package com.lovebird.api.dto.response.profile

import com.lovebird.api.dto.request.profile.AnniversaryResponse
import com.lovebird.api.util.ProfileUtils.getNextAnniversary
import com.lovebird.common.util.DateUtils.betweenDays
import com.lovebird.domain.dto.query.ProfilePartnerResponseParam
import com.lovebird.domain.dto.query.ProfileUserResponseParam
import java.time.LocalDate

data class ProfileDetailResponse(
	val userId: Long,
	val partnerId: Long?,
	val email: String,
	val nickname: String,
	val partnerNickname: String?,
	val firstDate: LocalDate?,
	val birthday: LocalDate?,
	val partnerBirthday: LocalDate?,
	val dayCount: Long?,
	val nextAnniversary: AnniversaryResponse?,
	val profileImageUrl: String?,
	val partnerImageUrl: String?
) {
	companion object {
		fun of(user: ProfileUserResponseParam, partner: ProfilePartnerResponseParam?): ProfileDetailResponse {
			return ProfileDetailResponse(
				userId = user.userId,
				partnerId = partner?.partnerId,
				email = user.email,
				nickname = user.nickname,
				partnerNickname = partner?.partnerNickname,
				firstDate = user.firstDate,
				birthday = user.birthday,
				partnerBirthday = partner?.partnerBirthday,
				dayCount = user.firstDate?.let { betweenDays(it, LocalDate.now()) + 1 },
				nextAnniversary = user.firstDate?.let { getNextAnniversary(it) },
				profileImageUrl = user.profileImageUrl,
				partnerImageUrl = partner?.partnerImageUrl
			)
		}
	}
}
