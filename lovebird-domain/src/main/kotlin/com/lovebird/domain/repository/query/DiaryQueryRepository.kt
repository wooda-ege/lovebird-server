package com.lovebird.domain.repository.query

import com.lovebird.domain.dto.query.DiaryListRequestParam
import com.lovebird.domain.dto.query.DiaryResponseParam
import com.lovebird.domain.dto.query.DiarySimpleRequestParam
import com.lovebird.domain.dto.query.DiarySimpleResponseParam
import com.lovebird.domain.entity.QDiary
import com.lovebird.domain.entity.QDiary.diary
import com.lovebird.domain.entity.QDiaryImage.diaryImage
import com.lovebird.domain.entity.QUser.user
import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class DiaryQueryRepository(
	private val queryFactory: JPAQueryFactory
) {

	fun findBeforeNowUsingCursor(param: DiaryListRequestParam): List<DiaryResponseParam> {
		return queryFactory
			.from(diary)
			.innerJoin(user)
			.on(eqUserId(user.id))
			.where(
				eqCouple(param.userId, param.partnerId),
				eqMemoryDateAndGtDiaryId(param.memoryDate, param.diaryId),
				ltMemoryDate(param.memoryDate)
			)
			.orderBy(descMemoryDate(), ascDiaryId())
			.limit(param.pageSize)
			.transform(
				groupBy(diary.id)
					.list(
						Projections.constructor(
							DiaryResponseParam::class.java,
							diary.id,
							user.id,
							diary.title,
							diary.memoryDate,
							diary.place,
							diary.content,
							list(
								Projections.constructor(
									String::class.java,
									diaryImage.imageUrl
								)
							)
						)
					)
			)
	}

	fun findAfterNowUsingCursor(param: DiaryListRequestParam): List<DiaryResponseParam> {
		return queryFactory
			.from(diary)
			.innerJoin(user)
			.on(eqUserId(user.id))
			.where(
				eqCouple(param.userId, param.partnerId),
				eqMemoryDateAndGtDiaryId(param.memoryDate, param.diaryId),
				gtMemoryDate(param.memoryDate)
			)
			.orderBy(ascMemoryDate(), ascDiaryId())
			.limit(param.pageSize)
			.transform(
				groupBy(diary.id)
					.list(
						Projections.constructor(
							DiaryResponseParam::class.java,
							diary.id,
							user.id,
							diary.title,
							diary.memoryDate,
							diary.place,
							diary.content,
							list(
								Projections.constructor(
									String::class.java,
									diaryImage.imageUrl
								)
							)
						)
					)
			)
	}

	fun findAllByMemoryDate(param: DiarySimpleRequestParam): List<DiarySimpleResponseParam> {
		return queryFactory
			.select(
				Projections.constructor(
					DiarySimpleResponseParam::class.java,
					diary.id,
					diary.user.id,
					diary.title,
					diary.memoryDate,
					diary.place,
					diary.content,
					diary.diaryImages.get(0)
				)
			)
			.from(diary)
			.innerJoin(user)
			.on(eqUserId(user.id))
			.where(eqCouple(param.userId, param.partnerId), eqMemoryDate(param.memoryDate))
			.orderBy(ascDiaryId())
			.fetch()
	}

	private fun eqDiary(diary: QDiary): BooleanExpression = diary.eq(diary)

	private fun eqCouple(userId: Long, partnerId: Long?): BooleanExpression {
		val expression: BooleanExpression = eqUserId(userId)

		return if (partnerId != null) {
			expression.or(eqUserId(partnerId))
		} else {
			expression
		}
	}

	private fun eqUserId(userId: Long): BooleanExpression = diary.user.id.eq(userId)

	private fun eqUserId(userId: NumberPath<Long>): BooleanExpression = diary.user.id.eq(userId)

	private fun eqMemoryDateAndGtDiaryId(memoryDate: LocalDate, diaryId: Long?): BooleanExpression {
		return gtDiaryId(diaryId).and(eqMemoryDate(memoryDate))
	}

	private fun eqMemoryDate(memoryDate: LocalDate): BooleanExpression = diary.memoryDate.eq(memoryDate)

	private fun gtDiaryId(diaryId: Long?): BooleanExpression = diary.id.gt(diaryId)

	private fun ltMemoryDate(memoryDate: LocalDate): BooleanExpression = diary.memoryDate.lt(memoryDate)

	private fun gtMemoryDate(memoryDate: LocalDate): BooleanExpression = diary.memoryDate.gt(memoryDate)

	private fun ascDiaryId(): OrderSpecifier<Long> = diary.id.asc()

	private fun ascMemoryDate(): OrderSpecifier<LocalDate> = diary.memoryDate.asc()

	private fun descMemoryDate(): OrderSpecifier<LocalDate> = diary.memoryDate.desc()
}
