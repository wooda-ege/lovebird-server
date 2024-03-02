package com.lovebird.domain.repository.query

import com.lovebird.domain.dto.query.CalendarListResponseParam
import com.lovebird.domain.dto.query.CalenderListRequestParam
import com.lovebird.domain.dto.query.QCalendarListResponseParam
import com.lovebird.domain.entity.QCalendar.calendar
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
				QCalendarListResponseParam(
					calendar.id,
					calendar.user.id,
					calendar.title,
					calendar.memo,
					calendar.color,
					calendar.alarm,
					calendar.startDate,
					calendar.endDate,
					calendar.startTime,
					calendar.endTime
				)
			)
			.from(calendar)
			.where(eqUserIdOrEqPartnerId(param))
			.where(eqYearAndMonth(param))
			.orderBy(calendar.startDate.asc())
			.fetch()
	}

	private fun eqUserIdOrEqPartnerId(calenderListRequestParam: CalenderListRequestParam): BooleanExpression {
		return eqUserId(calenderListRequestParam.userId).or(calenderListRequestParam.partnerId?.let { eqUserId(it) })
	}

	private fun eqUserId(userId: Long?): BooleanExpression = calendar.user.id.eq(userId)

	private fun eqYearAndMonth(param: CalenderListRequestParam): BooleanExpression? {
		if (param.year == null && param.month == null) {
			return null
		} else if (param.month == null) {
			return calendar.startDate.year().eq(param.year)
		} else if (param.year == null) {
			return calendar.startDate.month().eq(param.month)
		}
		return calendar.startDate.year().eq(param.year).and(calendar.startDate.month().eq(param.month))
	}
}
