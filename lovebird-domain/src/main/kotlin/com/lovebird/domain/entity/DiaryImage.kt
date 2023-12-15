package com.lovebird.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(
	name = "diary_image",
	indexes = [
		Index(name = "fk_diary_image_diary_id", columnList = "diary_id")
	]
)
class DiaryImage(
	diary: Diary,
	imageUrl: String
) {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "diary_image_id")
	val id: Long? = null

	@Column(name = "image_url", nullable = false)
	val imageUrl: String = imageUrl

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diary_id", referencedColumnName = "diary_id", nullable = false)
	val diary: Diary = diary
}
