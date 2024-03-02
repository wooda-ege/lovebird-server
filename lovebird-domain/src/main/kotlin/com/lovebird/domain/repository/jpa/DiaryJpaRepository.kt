package com.lovebird.domain.repository.jpa

import com.lovebird.domain.entity.Diary
import com.lovebird.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DiaryJpaRepository : JpaRepository<Diary, Long> {
	fun findAllByUser(user: User): List<Diary>
}
