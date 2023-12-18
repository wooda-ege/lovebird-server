package com.lovebird.domain.entity

import com.lovebird.common.enums.Alarm
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
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime

@DynamicUpdate
@Entity
@Table(name = "calendar_event")
class CalendarEvent(
	calendar: Calendar,
	user: User,
	eventAt: LocalDateTime,
	alarm: Alarm
) : AuditEntity() {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "calendar_event_id")
	val id: Long? = null

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
	val user: User = user

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "calendar_id", referencedColumnName = "calendar_id", nullable = false)
	val calendar: Calendar = calendar

	@Column(name = "send_flag")
	val sendFlag: Boolean = false

	@Column(name = "event_at")
	val eventAt: LocalDateTime = eventAt

	@Column(name = "alarm")
	@Enumerated(value = EnumType.STRING)
	val alarm: Alarm = alarm

	@Column(name = "result")
	val result: String = "알림 발송 전"
}
