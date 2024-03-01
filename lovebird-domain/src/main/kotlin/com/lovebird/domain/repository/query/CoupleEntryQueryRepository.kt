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

	fun findPartnerIdById(id: Long): Long {
		return queryFactory
			.select(coupleEntry.partner.id)
			.from(coupleEntry)
			.where(eqCoupleEntryId(id))
			.fetchOne()!!
	}

	fun eqCoupleEntryId(coupleEntryId: Long): BooleanExpression = coupleEntry.id.eq(coupleEntryId)

	fun eqUserId(userId: Long): BooleanExpression = coupleEntry.user.id.eq(userId)
}
