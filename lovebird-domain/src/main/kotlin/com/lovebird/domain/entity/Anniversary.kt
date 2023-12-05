package com.lovebird.domain.entity

import com.lovebird.common.enums.AnniversaryType
import com.lovebird.domain.entity.audit.AuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "anniversary")
class Anniversary(
	profile: Profile,
	type: AnniversaryType,
	date: LocalDate
) : AuditEntity() {

	@Id
	@Column(name = "anniversary_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null

	@Column(name = "anniversary_type", nullable = false)
	var type: AnniversaryType = type

	@Column(name = "anniversary_date", nullable = false)
	var date: LocalDate = date

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_id", referencedColumnName = "profile_id")
	val profile: Profile = profile
}
