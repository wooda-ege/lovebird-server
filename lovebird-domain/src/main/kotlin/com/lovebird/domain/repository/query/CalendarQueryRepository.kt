package com.lovebird.domain.repository.query

import com.lovebird.domain.dto.query.CalendarListResponseParam
import com.lovebird.domain.entity.QCalendar.calendar
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class CalendarQueryRepository(
	private val queryFactory: JPAQueryFactory
) {

	fun findCalendarsByDateAndUserIdAndPartnerId(
		year: Int,
		month: Int,
		userId: Long,
		partnerId: Long
	): List<CalendarListResponseParam> {
		return queryFactory
			.select(
				Projections.constructor(
					CalendarListResponseParam::class.java,
					calendar.id,
					calendar.user.id,
					calendar.title,
					calendar.memo,
					calendar.startDate,
					calendar.endDate,
					calendar.startTime,
					calendar.endTime,
					calendar.color,
					calendar.alarm
				)
			)
			.from(calendar)
			.where(eqUserIdOrEqPartnerId(userId, partnerId), eqYearAndMonth(year, month))
			.orderBy(calendar.startDate.asc())
			.fetch()
	}

	private fun eqUserIdOrEqPartnerId(userId: Long, partnerId: Long): BooleanExpression {
		return calendar.user.id.eq(userId).or(calendar.user.id.eq(partnerId))
	}

	private fun eqYearAndMonth(year: Int, month: Int): BooleanExpression {
		return calendar.startDate.year().eq(year).and(calendar.startDate.month().eq(month))
	}
}
