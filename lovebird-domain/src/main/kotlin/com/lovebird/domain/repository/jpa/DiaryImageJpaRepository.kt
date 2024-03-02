package com.lovebird.domain.repository.jpa

import com.lovebird.domain.entity.Diary
import com.lovebird.domain.entity.DiaryImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DiaryImageJpaRepository : JpaRepository<DiaryImage, Long> {
	fun deleteAllByDiaryIn(diary: List<Diary>)
}
