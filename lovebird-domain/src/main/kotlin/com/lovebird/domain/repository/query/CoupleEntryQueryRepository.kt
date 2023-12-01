package com.lovebird.domain.repository.query

import com.lovebird.domain.entity.CoupleEntry
import com.lovebird.domain.entity.QCoupleEntry.coupleEntry
import com.lovebird.domain.entity.User
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class CoupleEntryQueryRepository(
	private val queryFactory: JPAQueryFactory
) {

	fun findByUser(user: User): CoupleEntry? {
		return queryFactory
			.selectFrom(coupleEntry)
			.where(eqUser(user))
			.fetchFirst()
	}

	fun eqUser(user: User): BooleanExpression? {
		return coupleEntry.user.eq(user).or(coupleEntry.partner.eq(user))
	}
}
