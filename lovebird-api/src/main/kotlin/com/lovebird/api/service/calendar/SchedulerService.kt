package com.lovebird.api.service.calendar

import com.lovebird.common.enums.AlarmResponse
import com.lovebird.domain.entity.Calendar
import com.lovebird.domain.entity.CalendarEvent
import com.lovebird.domain.repository.reader.CalendarEventReader
import com.lovebird.fcm.dto.param.FcmNotificationParam
import com.lovebird.fcm.service.FcmService
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class SchedulerService(
	private val taskScheduler: TaskScheduler,
	private val fcmService: FcmService,
	private val calendarEventReader: CalendarEventReader
) {
	companion object {
		const val ALARM_TITLE = "[LoveBird] 예정된 일정이 있어요!"
	}

	fun registryCalendarAlarm(calendar: Calendar) {
		val calendarEvents: List<CalendarEvent> = calendarEventReader.findCalendarEventsByCalendar(calendar)

		if (calendarEvents.isNotEmpty()) {
			val localDateTime: LocalDateTime = calendarEvents.first().eventAt

			taskScheduler.schedule(setTask(calendar), localDateTime.toInstant(ZoneOffset.UTC))
		}
	}

	@Transactional
	fun setTask(calendar: Calendar): Runnable {
		return Runnable {
			val calendarEvents: List<CalendarEvent> = calendarEventReader.findCalendarEventsByCalendar(calendar)

			if (calendarEvents.isNotEmpty()) {
				val localDateTime: LocalDateTime = LocalDateTime.now()

				// Instant 시간에 실행 될 시, 시간 검증
				if (isSameTime(calendarEvents.first(), localDateTime)) {
					val deviceTokens = calendarEvents.stream().map { it.user.deviceToken!! }.toList()

					fcmService.sendNotificationAsync(
						FcmNotificationParam(
							deviceTokens = deviceTokens,
							title = ALARM_TITLE,
							body = AlarmResponse.ALARM_SEND_SUCCESS.message,
							data = mutableListOf()
						)
					)

					// 플래그 설정
					calendarEvents.forEach {
						it.setFlag()
					}
				}
			}
		}
	}

	private fun isSameTime(calendarEvent: CalendarEvent, localDateTime: LocalDateTime): Boolean {
		return calendarEvent.eventAt.year == localDateTime.year &&
			calendarEvent.eventAt.month == localDateTime.month &&
			calendarEvent.eventAt.dayOfMonth == localDateTime.dayOfMonth &&
			calendarEvent.eventAt.minute == localDateTime.minute
	}
}
