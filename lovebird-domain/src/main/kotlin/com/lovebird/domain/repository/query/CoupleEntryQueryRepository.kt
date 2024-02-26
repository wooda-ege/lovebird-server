package com.lovebird.domain.repository.query

import com.lovebird.domain.entity.CoupleEntry
import com.lovebird.domain.entity.QCoupleEntry.coupleEntry
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class CoupleEntryQueryRepository(
	private val queryFactory: JPAQueryFactory
) {

	fun findByUserId(userId: Long): CoupleEntry? {
		return queryFactory
			.selectFrom(coupleEntry)
			.where(eqUserId(userId))
			.fetchOne()
	}

	fun eqUserId(userId: Long): BooleanExpression = coupleEntry.user.id.eq(userId)
}
