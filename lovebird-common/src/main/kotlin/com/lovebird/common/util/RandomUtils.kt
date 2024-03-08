package com.lovebird.common.util

import java.time.LocalDateTime
import java.util.UUID

object RandomUtils {

	private const val ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz"

	fun generateCode(): String {
		val random = java.util.Random()

		return (1..8)
			.map { ALPHA_NUMERIC_STRING[random.nextInt(ALPHA_NUMERIC_STRING.length)] }
			.joinToString("")
	}

	fun generateTimeBasedUUID(): String {
		return UUID.fromString(LocalDateTime.now().toString()).toString()
	}
}
