package com.lovebird.domain.repository.query
import java.time.LocalDate
import org.springframework.stereotype.Repository
import com.lovebird.domain.dto.query.DiaryListRequestParam
import com.lovebird.domain.dto.query.DiaryResponseParam
import com.lovebird.domain.dto.query.DiarySimpleRequestParam
import com.lovebird.domain.entity.Diary
import com.lovebird.domain.entity.QDiary
import com.lovebird.domain.entity.QDiary.diary
import com.lovebird.domain.entity.QDiaryImage.diaryImage
import com.querydsl.core.group.GroupBy.list
import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.jpa.impl.JPAQueryFactory

@Repository
class DiaryQueryRepository(
	private val queryFactory: JPAQueryFactory
) {

	fun findBeforeNowUsingCursor(param: DiaryListRequestParam): List<DiaryResponseParam> {
		val transform: Map<Diary, List<String>> = queryFactory
			.from(diary)
			.leftJoin(diary.diaryImages, diaryImage)
			.where(
				eqCouple(param.userId, param.partnerId),
				eqMemoryDateAndGtDiaryId(param.memoryDate, param.diaryId),
				ltMemoryDate(param.memoryDate)
			)
			.orderBy(descMemoryDate(), ascDiaryId())
			.limit(param.pageSize)
			.transform(groupBy(diary).`as`(list(diaryImage.imageUrl)))

		return DiaryResponseParam.of(transform)
	}

	fun findAfterNowUsingCursor(param: DiaryListRequestParam): List<DiaryResponseParam> {
		val transform: Map<Diary, List<String>> = queryFactory
			.from(diary)
			.leftJoin(diary.diaryImages, diaryImage)
			.where(
				eqCouple(param.userId, param.partnerId),
				eqMemoryDateAndGtDiaryId(param.memoryDate, param.diaryId),
				gtMemoryDate(param.memoryDate)
			)
			.orderBy(ascMemoryDate(), ascDiaryId())
			.limit(param.pageSize)
			.transform(groupBy(diary).`as`(list(diaryImage.imageUrl)))

		return DiaryResponseParam.of(transform)
	}

	fun findAllByMemoryDate(param: DiarySimpleRequestParam): List<DiaryResponseParam> {
		val transform: Map<Diary, List<String>> = queryFactory
			.from(diary)
			.leftJoin(diary.diaryImages, diaryImage)
			.where(eqCouple(param.userId, param.partnerId), eqMemoryDate(param.memoryDate))
			.orderBy(ascDiaryId())
			.transform(groupBy(diary).`as`(list(diaryImage.imageUrl)))

		return DiaryResponseParam.of(transform)
	}

	fun findAll(userId: Long, partnerId: Long?): List<DiaryResponseParam> {
		val transform: Map<Diary, List<String>> = queryFactory
			.from(diary)
			.leftJoin(diary.diaryImages, diaryImage)
			.where(eqCouple(userId, partnerId))
			.orderBy(ascDiaryId())
			.transform(groupBy(diary).`as`(list(diaryImage.imageUrl)))

		return DiaryResponseParam.of(transform)
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
