package com.lovebird.domain.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.lovebird.domain.dto.command.DiaryUpdateRequestParam
import com.lovebird.domain.entity.audit.AuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

@Entity
@Table(name = "diary")
class Diary(
	user: User,
	title: String,
	memoryDate: LocalDate,
	place: String?,
	content: String?
) : AuditEntity() {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "diary_id")
	val id: Long? = null

	@Column(name = "title", nullable = false)
	var title: String = title

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "memory_date", nullable = false)
	var memoryDate: LocalDate = memoryDate

	@Column(name = "place")
	var place: String? = place

	@Column(name = "content")
	var content: String? = content

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
	val user: User = user

	@OneToMany(mappedBy = "diary", fetch = FetchType.LAZY)
	val diaryImages: List<DiaryImage> = mutableListOf()

	fun update(param: DiaryUpdateRequestParam) {
		param.title?.let { this.title = it }
		param.memoryDate?.let { this.memoryDate = it }
		param.place?.let { this.place = it }
		param.content?.let { this.content = it }
	}
}
