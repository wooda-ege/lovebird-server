package com.lovebird.domain.repository.query

import com.lovebird.domain.dto.query.ProfileDetailResponseParam
import com.lovebird.domain.entity.QCoupleEntry.coupleEntry
import com.lovebird.domain.entity.QProfile.profile
import com.lovebird.domain.entity.User
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class ProfileQueryRepository(
	private val queryFactory: JPAQueryFactory
) {

	fun findDetailParamByUser(user: User): ProfileDetailResponseParam? {
		return queryFactory
			.select(
				Projections.constructor(
					ProfileDetailResponseParam::class.java,
					profile.id,
					coupleEntry.partner.id,
					profile.email,
					profile.nickname,
					coupleEntry.partner.profile.nickname,
					profile.firstDate,
					profile.birthday,
					coupleEntry.partner.profile.birthday,
					profile.imageUrl,
					coupleEntry.partner.profile.imageUrl
				)
			)
			.from(profile)
			.innerJoin(coupleEntry)
			.on(coupleEntry.user.eq(user))
			.where(eqUser(user))
			.fetchFirst()
	}

	private fun eqUser(user: User): BooleanExpression {
		return profile.user.eq(user)
	}
}
