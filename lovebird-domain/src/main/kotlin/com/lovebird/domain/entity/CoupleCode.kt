package com.lovebird.domain.entity

import com.lovebird.domain.entity.audit.AuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Entity
@Table(name = "couple_code")
class CoupleCode(
	user: User,
	code: String
) : AuditEntity() {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "couple_code_id")
	var id: Long? = null

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	var user: User = user

	@Column(name = "code", nullable = false, unique = true)
	var code: String = code

	fun isExpired(): Boolean {
		return ChronoUnit.HOURS.between(createdAt, LocalDateTime.now()) >= 24
	}

	fun getRemainSeconds(): Long {
		return ChronoUnit.SECONDS.between(createdAt, LocalDateTime.now())
	}
}
