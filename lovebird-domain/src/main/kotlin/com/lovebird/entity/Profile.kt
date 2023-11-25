package com.lovebird.entity

import com.lovebird.entity.audit.AuditEntity
import com.lovebird.enums.Gender
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
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDate

@Entity
@Table(name = "profile")
@DynamicUpdate
class Profile(
	user: User,
	imageUrl: String,
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

	@Column(name = "partner_id")
	var partnerId: Long? = null

	@Column(name = "image_url", nullable = false)
	var imageUrl: String = imageUrl

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

	@Column(name = "linked_flag", nullable = false)
	var linkedFlag: Boolean = false
}
