package com.lovebird.domain.repository.jpa

import com.lovebird.domain.entity.CoupleEntry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CoupleEntryJpaRepository : JpaRepository<CoupleEntry, Long>
