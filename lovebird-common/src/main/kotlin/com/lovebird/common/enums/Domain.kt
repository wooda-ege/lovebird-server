package com.lovebird.common.enums

enum class Domain {

	PROFILE,
	DIARY;

	fun lower(): String {
		return this.name.lowercase()
	}
}
