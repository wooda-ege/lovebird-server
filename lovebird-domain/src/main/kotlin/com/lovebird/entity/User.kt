package com.lovebird.entity

import com.lovebird.entity.audit.AuditEntity
import com.lovebird.enums.Provider
import com.lovebird.enums.Role
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
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

	fun updateDeviceToken(deviceToken: String?) {
		this.deviceToken = deviceToken
	}
}
