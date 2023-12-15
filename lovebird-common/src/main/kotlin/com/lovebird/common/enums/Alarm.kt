package com.lovebird.common.enums

enum class Alarm(
	val value: Long
) {

	TYPE_A(5L), // 5분 전
	TYPE_B(15L), // 15분 전
	TYPE_C(30L), // 30분 전
	TYPE_D(60L * 1L), // 1시간 전
	TYPE_E(60L * 2L), // 2시간 전
	TYPE_F(60L * 24L * 1L), // 1일 전
	TYPE_G(60L * 24L * 2L), // 2일 전
	NONE(0L);
}
