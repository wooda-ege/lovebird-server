package com.lovebird.domain.repository.jpa

import com.lovebird.domain.entity.Calendar
import com.lovebird.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CalendarJpaRepository : JpaRepository<Calendar, Long> {

	fun findCalendarByIdAndUser(id: Long, user: User): Calendar
}
