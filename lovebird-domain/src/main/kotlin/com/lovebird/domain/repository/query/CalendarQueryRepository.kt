package com.lovebird.domain.repository.query

import com.lovebird.domain.dto.query.CalendarListResponseParam
import com.lovebird.domain.dto.query.CalenderListRequestParam
import com.lovebird.domain.entity.QCalendar.calendar
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class CalendarQueryRepository(
	private val queryFactory: JPAQueryFactory
) {

	fun findCalendarsByDateAndUserIdAndPartnerId(param: CalenderListRequestParam): List<CalendarListResponseParam> {
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
			.where(eqUserIdOrEqPartnerId(param))
			.where(eqYearAndMonth(param))
			.orderBy(calendar.startDate.asc())
			.fetch()
	}

	private fun eqUserIdOrEqPartnerId(calenderListRequestParam: CalenderListRequestParam): BooleanExpression {
		return eqUserId(calenderListRequestParam.userId).or(eqUserId(calenderListRequestParam.partnerId))
	}

	private fun eqUserId(userId: Long?): BooleanExpression = calendar.user.id.eq(userId)

	private fun eqYearAndMonth(param: CalenderListRequestParam): BooleanExpression {
		return calendar.startDate.year().eq(param.year).and(calendar.startDate.month().eq(param.month))
	}
}
