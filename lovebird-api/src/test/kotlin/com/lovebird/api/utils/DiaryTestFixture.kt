package com.lovebird.api.utils

import com.lovebird.api.dto.param.diary.DiaryCreateParam
import com.lovebird.api.dto.param.diary.DiaryUpdateParam
import com.lovebird.api.dto.request.diary.DiaryCreateRequest
import com.lovebird.api.dto.request.diary.DiaryListRequest
import com.lovebird.api.dto.request.diary.DiaryUpdateRequest
import com.lovebird.common.enums.DiarySearchType
import com.lovebird.domain.dto.query.DiaryResponseParam
import com.lovebird.domain.dto.query.DiarySimpleResponseParam
import com.lovebird.domain.entity.Diary
import com.lovebird.domain.entity.User
import org.springframework.test.util.ReflectionTestUtils
import java.time.LocalDate
import kotlin.random.Random

object DiaryTestFixture {
	fun getDiaries(size: Long): List<DiaryResponseParam> {
		val diaries = arrayListOf<DiaryResponseParam>()

		for (i in 1..size) {
			diaries.add(
				DiaryResponseParam(
					diaryId = i,
					userId = i,
					title = "제목$i",
					memoryDate = LocalDate.now(),
					place = null,
					content = "내용$i",
					imageUrls = emptyList()
				)
			)
		}
		return diaries
	}

	fun getDiaryByCreateParam(param: DiaryCreateParam, user: User): Diary {
		return Diary(
			user = user,
			title = param.title,
			memoryDate = param.memoryDate,
			place = param.place,
			content = param.content
		)
	}

	fun getDiaryByUpdateParam(param: DiaryUpdateParam, user: User): Diary {
		return Diary(
			user = user,
			title = param.title,
			memoryDate = param.memoryDate,
			content = param.content,
			place = param.place
		)
	}

	fun getDiaryCreateRequest(): DiaryCreateRequest {
		return DiaryCreateRequest(
			title = "다이어리 제목",
			memoryDate = LocalDate.now(),
			place = "장소",
			content = "내용",
			imageUrls = mutableListOf("imageUrl1", "imageUrl2")
		)
	}

	fun getDiaryUpdateRequest(): DiaryUpdateRequest {
		return DiaryUpdateRequest(
			title = "다이어리 제목 수정",
			memoryDate = LocalDate.now(),
			place = "장소",
			content = "내용 수정",
			imageUrls = mutableListOf("imageUrl1", "imageUrl2")
		)
	}

	fun getCreateParam(imageUrls: List<String>?, user: User): DiaryCreateParam {
		return DiaryCreateParam(
			user = user,
			title = "다이어리 제목",
			memoryDate = LocalDate.now(),
			place = null,
			content = "다이어리 콘텐츠",
			imageUrls = imageUrls
		)
	}

	fun getUpdateParam(imageUrls: List<String>?): DiaryUpdateParam {
		return DiaryUpdateParam(
			diaryId = 1L,
			title = "제목 업데이트",
			memoryDate = LocalDate.now(),
			place = "장소 업데이트",
			content = "내용 업데이트",
			imageUrls = imageUrls
		)
	}

	fun getSearchByCursorRequest(searchType: DiarySearchType, diaryId: Long, pageSize: Long): DiaryListRequest.SearchByCursorRequest {
		return DiaryListRequest.SearchByCursorRequest(
			memoryDate = LocalDate.now(),
			searchType = searchType,
			diaryId = diaryId,
			pageSize = pageSize
		)
	}

	fun getDiaryByUser(user: User): Diary {
		val diary = Diary(
			user = user,
			title = "다이어리 제목",
			memoryDate = LocalDate.now(),
			place = "장소",
			content = "내용"
		)

		ReflectionTestUtils.setField(diary, "id", 1L)

		return diary
	}

	fun getSearchByMemoryDateRequest(memoryDate: LocalDate): DiaryListRequest.SearchByMemoryDateRequest {
		return DiaryListRequest.SearchByMemoryDateRequest(
			memoryDate = memoryDate
		)
	}

	fun getDiarySimpleResponseList(user: User, partner: User?, size: Int): List<DiarySimpleResponseParam> {
		val diaries = arrayListOf<DiarySimpleResponseParam>()

		for (i in 1..size) {
			diaries.add(
				DiarySimpleResponseParam(
					diaryId = i.toLong(),
					userId = getRandomUserId(user, partner),
					title = "제목$i",
					memoryDate = LocalDate.now(),
					place = "장소$i",
					content = "내용$i",
					imageUrl = "imageURL$i"
				)
			)
		}

		return diaries
	}

	private fun getRandomUserId(user: User, partner: User?): Long {
		val list = mutableListOf(user)
		partner?.let {
			list.add(it)
		}

		val randomIndex = Random.nextInt(list.size)

		return list[randomIndex].id!!
	}
}
