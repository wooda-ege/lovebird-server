package com.lovebird.api.dto.response.profile

import com.lovebird.api.dto.request.profile.AnniversaryResponse
import com.lovebird.domain.dto.query.ProfileDetailResponseParam
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class ProfileDetailResponse(
	val userId: Long,
	val partnerId: Long,
	val email: String,
	val nickname: String,
	val partnerNickname: String?,
	val firstDate: LocalDate?,
	val birthday: LocalDate?,
	val dayCount: Long?,
	val nextAnniversary: AnniversaryResponse?,
	val profileImageUrl: String,
	val partnerImageUrl: String?
) {
	companion object {
		@JvmStatic
		fun of(param: ProfileDetailResponseParam): ProfileDetailResponse {
			return ProfileDetailResponse(
				userId = param.userId,
				partnerId = param.partnerId,
				email = param.email,
				nickname = param.nickname,
				partnerNickname = param.partnerNickname,
				firstDate = param.firstDate,
				birthday = param.birthday,
				dayCount = param.firstDate?.let { ChronoUnit.DAYS.between(it, LocalDate.now()) },
				nextAnniversary = param.nextAnniversaryType?.let {
					AnniversaryResponse(it, param.nextAnniversaryDate!!)
				},
				profileImageUrl = param.profileImageUrl,
				partnerImageUrl = param.partnerImageUrl
			)
		}
	}
}
