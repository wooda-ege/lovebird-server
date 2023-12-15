package com.lovebird.domain.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.lovebird.common.enums.Alarm
import com.lovebird.common.enums.Color
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
import org.hibernate.annotations.ColumnDefault
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "calendar")
class Calendar(
	title: String,
	memo: String?,
	startDate: LocalDate,
	endDate: LocalDate?,
	startTime: LocalTime?,
	endTime: LocalTime?,
	color: Color?,
	alarm: Alarm?,
	user: User
) : AuditEntity() {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "calendar_id")
	val id: Long? = null

	@Column(name = "title", nullable = false)
	var title: String = title

	@Column(name = "memo")
	var memo: String? = memo

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "start_date", nullable = false)
	var startDate: LocalDate = startDate

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "end_date")
	var endDate: LocalDate? = endDate

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
	@DateTimeFormat(pattern = "HH:mm")
	@Column(name = "start_time")
	var startTime: LocalTime? = startTime

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
	@DateTimeFormat(pattern = "HH:mm")
	@Column(name = "end_time")
	var endTime: LocalTime? = endTime

	@Column(name = "color", nullable = false)
	@ColumnDefault("'PRIMARY'")
	@Enumerated(value = EnumType.STRING)
	var color: Color? = color

	@Column(name = "alarm")
	@ColumnDefault("'NONE'")
	@Enumerated(value = EnumType.STRING)
	var alarm: Alarm? = alarm

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
	val user: User = user
}
