package com.lovebird.domain.repository.jpa

import com.lovebird.domain.entity.Profile
import com.lovebird.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfileJpaRepository : JpaRepository<Profile, Long> {

	fun findByUser(user: User): Profile?
}
