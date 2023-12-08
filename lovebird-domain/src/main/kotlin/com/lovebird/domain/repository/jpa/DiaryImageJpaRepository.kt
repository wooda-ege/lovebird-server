package com.lovebird.domain.repository.jpa

import com.lovebird.domain.entity.DiaryImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DiaryImageJpaRepository : JpaRepository<DiaryImage, Long>
