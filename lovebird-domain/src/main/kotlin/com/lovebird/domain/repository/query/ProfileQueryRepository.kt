package com.lovebird.domain.repository.query

import com.lovebird.domain.dto.query.ProfilePartnerResponseParam
import com.lovebird.domain.dto.query.ProfileUserResponseParam
import com.lovebird.domain.entity.QCoupleEntry.coupleEntry
import com.lovebird.domain.entity.QProfile.profile
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class ProfileQueryRepository(
	private val queryFactory: JPAQueryFactory
) {

	fun findDetailUserParamByUser(userId: Long): ProfileUserResponseParam? {
		return queryFactory
			.select(
				Projections.constructor(
					ProfileUserResponseParam::class.java,
					profile.user.id,
					coupleEntry.id,
					profile.email,
					profile.nickname,
					profile.firstDate,
					profile.birthday,
					profile.imageUrl
				)
			)
			.from(profile)
			.leftJoin(coupleEntry)
			.on(profile.user.eq(coupleEntry.user))
			.where(eqUserId(userId))
			.fetchOne()
	}

	fun findDetailPartnerParamByUser(partnerId: Long): ProfilePartnerResponseParam? {
		return queryFactory
			.select(
				Projections.constructor(
					ProfilePartnerResponseParam::class.java,
					profile.user.id,
					profile.email,
					profile.nickname,
					profile.firstDate,
					profile.birthday,
					profile.imageUrl
				)
			)
			.from(profile)
			.innerJoin(coupleEntry)
			.on(profile.user.eq(coupleEntry.user))
			.where(eqUserId(partnerId))
			.fetchOne()
	}

	private fun eqUserId(userId: Long): BooleanExpression {
		return profile.user.id.eq(userId)
	}
}
