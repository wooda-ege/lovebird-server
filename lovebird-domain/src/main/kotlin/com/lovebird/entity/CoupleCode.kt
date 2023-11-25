package com.lovebird.entity

import com.lovebird.entity.audit.AuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "couple_code")
class CoupleCode(
	user: User,
	code: String
) : AuditEntity() {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "couple_code_id")
	private var id: Long? = null

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private var user: User = user

	@Column(name = "code", nullable = false, unique = true)
	private var code: String = code

	@Column(name = "usage_flag", nullable = false)
	private var useCode: Boolean = false
}
