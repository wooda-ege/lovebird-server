package com.lovebird.domain.repository.jpa

import com.lovebird.domain.entity.Calendar
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CalendarJpaRepository : JpaRepository<Calendar, Long>
