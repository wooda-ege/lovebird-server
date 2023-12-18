package com.lovebird.domain.repository.jpa

import com.lovebird.domain.entity.CalendarEvent
import org.springframework.data.jpa.repository.JpaRepository

interface CalendarEventJpaRepository : JpaRepository<CalendarEvent, Long>
