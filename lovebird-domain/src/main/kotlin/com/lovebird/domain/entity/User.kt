package com.lovebird.domain.entity

import com.lovebird.common.enums.Provider
import com.lovebird.common.enums.Role
import com.lovebird.domain.entity.audit.AuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicUpdate

@Entity
@Table(name = "users")
@DynamicUpdate
class User(
	provider: Provider,
	providerId: String,
	deviceToken: String?
) : AuditEntity() {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null

	@Column(name = "provider", nullable = false)
	@Enumerated(EnumType.STRING)
	val provider: Provider = provider

	@Column(name = "provider_id", nullable = false)
	val providerId: String = providerId

	@Column(name = "device_token")
	var deviceToken: String? = deviceToken

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	val role: Role = Role.ROLE_USER

	@OneToOne(mappedBy = "user")
	var profile: Profile? = null

	@OneToMany(mappedBy = "user")
	val calendarEvents: List<CalendarEvent> = mutableListOf()

	fun updateDeviceToken(deviceToken: String?) {
		this.deviceToken = deviceToken
	}
}
