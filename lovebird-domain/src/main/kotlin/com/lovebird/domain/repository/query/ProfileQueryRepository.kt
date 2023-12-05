package com.lovebird.domain.repository.query

import com.lovebird.domain.dto.query.ProfileDetailParam
import com.lovebird.domain.entity.QAnniversary.anniversary
import com.lovebird.domain.entity.QCoupleEntry.coupleEntry
import com.lovebird.domain.entity.QProfile.profile
import com.lovebird.domain.entity.User
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class ProfileQueryRepository(
	private val queryFactory: JPAQueryFactory
) {

	fun findDetailParamByUser(user: User): ProfileDetailParam? {
		return queryFactory
			.select(
				Projections.constructor(
					ProfileDetailParam::class.java,
					profile.id,
					coupleEntry.partner.id,
					profile.email,
					profile.nickname,
					coupleEntry.partner.profile.nickname,
					profile.firstDate,
					profile.birthday,
					anniversary.type,
					anniversary.date,
					profile.imageUrl,
					coupleEntry.partner.profile.imageUrl
				)
			)
			.from(profile)
			.innerJoin(coupleEntry)
			.on(coupleEntry.user.eq(user))
			.innerJoin(anniversary)
			.on(anniversary.profile.eq(profile))
			.where(eqUser(user), goeNow())
			.orderBy(anniversary.date.asc())
			.fetchFirst()
	}

	private fun eqUser(user: User): BooleanExpression {
		return profile.user.eq(user)
	}

	private fun goeNow(): BooleanExpression {
		return anniversary.date.goe(LocalDate.now())
	}
}
