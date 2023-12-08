package com.lovebird.domain.repository.query

import com.lovebird.domain.dto.query.DiaryListRequestParam
import com.lovebird.domain.dto.query.DiaryResponseParam
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
import java.time.LocalDateTime

@Repository
class DiaryQueryRepository(
	private val queryFactory: JPAQueryFactory
) {

	fun findBeforeNowUsingCursor(param: DiaryListRequestParam): List<DiaryResponseParam> {
		return queryFactory
			.from(diary)
			.innerJoin(user)
			.on(eqUserId(user.id))
			.innerJoin(diaryImage)
			.on(eqDiary(diaryImage.diary))
			.where(eqCouple(param.userId, param.partnerId), loeMemoryDate(param.memoryDate))
			.orderBy(descMemoryDate(), descCreatedAt())
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
									diary.diaryImages.any().imageUrl
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
			.innerJoin(diaryImage)
			.on(eqDiary(diaryImage.diary))
			.where(eqCouple(param.userId, param.partnerId), goeMemoryDate(param.memoryDate))
			.orderBy(ascMemoryDate(), ascCreatedAt())
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
									diary.diaryImages.any().imageUrl
								)
							)
						)
					)
			)
	}

	private fun eqDiary(diary: QDiary): BooleanExpression = diary.eq(diary)

	private fun eqCouple(userId: Long, partnerId: Long): BooleanExpression = eqUserId(userId).or(eqUserId(partnerId))

	private fun eqUserId(userId: Long): BooleanExpression = diary.user.id.eq(userId)

	private fun eqUserId(userId: NumberPath<Long>): BooleanExpression = diary.user.id.eq(userId)

	private fun loeMemoryDate(memoryDate: LocalDate): BooleanExpression = diary.memoryDate.loe(memoryDate)

	private fun goeMemoryDate(memoryDate: LocalDate): BooleanExpression = diary.memoryDate.goe(memoryDate)

	private fun ascMemoryDate(): OrderSpecifier<LocalDate> = diary.memoryDate.asc()

	private fun descMemoryDate(): OrderSpecifier<LocalDate> = diary.memoryDate.desc()

	private fun ascCreatedAt(): OrderSpecifier<LocalDateTime> = diary.createdAt.asc()

	private fun descCreatedAt(): OrderSpecifier<LocalDateTime> = diary.createdAt.desc()
}
