package com.lovebird.api.service.user

import com.lovebird.api.service.calendar.CalendarService
import com.lovebird.api.service.couple.CoupleService
import com.lovebird.api.service.diary.DiaryService
import com.lovebird.api.service.profile.ProfileService
import com.lovebird.domain.entity.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthProcessingService(
	private val userService: UserService,
	private val diaryService: DiaryService,
	private val calendarService: CalendarService,
	private val coupleService: CoupleService,
	private val profileService: ProfileService
) {

	@Transactional
	fun deleteAccount(user: User) {
		diaryService.deleteByUser(user)
		calendarService.deleteByUser(user)
		coupleService.deleteByUser(user)
		profileService.deleteByUser(user)
		userService.deleteByUser(user)
	}
}
