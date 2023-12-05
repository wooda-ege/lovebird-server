package com.lovebird.fcm.dto.param

data class FcmNotificationParam(
	val deviceToken: String,
	val title: String,
	val body: String,
	val data: List<FcmDataParam>
)
