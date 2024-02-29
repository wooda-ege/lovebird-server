package com.lovebird.api.dto.response.profile

import com.lovebird.api.dto.request.profile.AnniversaryResponse
import com.lovebird.api.util.ProfileUtils.getNextAnniversary
import com.lovebird.common.util.DateUtils.betweenDays
import com.lovebird.domain.dto.query.ProfileDetailResponseParam
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
		fun from(param: ProfileDetailResponseParam): ProfileDetailResponse {
			return ProfileDetailResponse(
				userId = param.userId,
				partnerId = param.partnerId,
				email = param.email,
				nickname = param.nickname,
				partnerNickname = param.partnerNickname,
				firstDate = param.firstDate,
				birthday = param.birthday,
				partnerBirthday = param.partnerBirthday,
				dayCount = param.firstDate?.let { betweenDays(it, LocalDate.now()) + 1 },
				nextAnniversary = param.firstDate?.let { getNextAnniversary(it) },
				profileImageUrl = param.profileImageUrl,
				partnerImageUrl = param.partnerImageUrl
			)
		}
	}
}
