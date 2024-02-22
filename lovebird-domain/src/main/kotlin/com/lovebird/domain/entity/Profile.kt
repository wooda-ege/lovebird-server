package com.lovebird.domain.entity

import com.lovebird.common.enums.Gender
import com.lovebird.domain.dto.command.ProfileUpdateRequestParam
import com.lovebird.domain.entity.audit.AuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "profile")
class Profile(
	user: User,
	imageUrl: String?,
	email: String,
	nickname: String,
	birthday: LocalDate?,
	firstDate: LocalDate?,
	gender: Gender
) : AuditEntity() {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "profile_id")
	val id: Long? = null

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
	val user: User = user

	@Column(name = "image_url")
	var imageUrl: String? = imageUrl

	@Column(name = "email", nullable = false)
	var email: String = email

	@Column(name = "nickname", nullable = false)
	var nickname: String = nickname

	@Column(name = "birthday")
	var birthday: LocalDate? = birthday

	@Column(name = "first_date")
	var firstDate: LocalDate? = firstDate

	@Column(name = "gender", nullable = false)
	@Enumerated(value = EnumType.STRING)
	var gender: Gender = gender

	fun update(param: ProfileUpdateRequestParam) {
		param.imageUrl?.let { this.imageUrl = it }
		param.email?.let { this.email = it }
		param.nickname?.let { this.nickname = it }
		param.birthday?.let { this.birthday = it }
		param.firstDate?.let { this.firstDate = it }
		param.gender?.let { this.gender = it }
	}
}
