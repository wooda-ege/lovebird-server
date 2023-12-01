package com.lovebird.domain.repository.jpa

import com.lovebird.domain.entity.CoupleEntry
import org.springframework.data.jpa.repository.JpaRepository

interface CoupleJpaRepository : JpaRepository<CoupleEntry, Long> {
}
