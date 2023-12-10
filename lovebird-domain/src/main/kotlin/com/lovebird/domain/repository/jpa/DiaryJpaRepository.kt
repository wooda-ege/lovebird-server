package com.lovebird.domain.repository.jpa

import com.lovebird.domain.entity.Diary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DiaryJpaRepository : JpaRepository<Diary, Long>
