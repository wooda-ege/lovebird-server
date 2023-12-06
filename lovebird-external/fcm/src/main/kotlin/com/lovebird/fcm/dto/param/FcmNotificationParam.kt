package com.lovebird.fcm.dto.param

data class FcmNotificationParam(
	val deviceTokens: List<String>,
	val title: String,
	val body: String,
	val data: List<FcmDataParam>
) {
	fun toDataMap(): Map<String, String> {
		return data.associate { it.key to it.value }
	}
}
