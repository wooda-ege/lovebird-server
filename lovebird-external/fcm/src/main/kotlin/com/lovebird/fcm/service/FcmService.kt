package com.lovebird.fcm.service

import com.google.firebase.messaging.BatchResponse
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.google.firebase.messaging.SendResponse
import com.lovebird.fcm.dto.param.FcmNotificationParam
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FcmService(
	private val logger: Logger = LoggerFactory.getLogger(FcmService::class.java)
) {

	fun sendNotificationAsync(param: FcmNotificationParam) {
		runBlocking {
			sendAllNotification(param)
		}
	}

	suspend fun sendAllNotification(param: FcmNotificationParam) {
		try {
			val messages = getMessages(param)
			val response = FirebaseMessaging.getInstance().sendEach(messages)

			loggingFailToken(response, param.deviceTokens)
		} catch (e: FirebaseMessagingException) {
			logger.error("cannot send to memberList push message. error info : {${e.message}}");
		}
	}

	private fun loggingFailToken(response: BatchResponse, tokenList: List<String>) {
		when {
			response.failureCount > 0 -> {
				val responses: List<SendResponse> = response.responses;
				val failedTokens = mutableListOf<String>();

				responses.indices
					.filterNot { responses[it].isSuccessful }
					.forEach { failedTokens += tokenList[it] }
				logger.error("List of tokens are not valid FCM token : ${failedTokens.joinToString(", ")}")
			}
		}
	}

	private fun getMessages(param: FcmNotificationParam): List<Message> {
		val data = param.toDataMap()

		return param.deviceTokens.map {
			getMessage(it, getNotification(param.title, param.body), data)
		}
	}

	private fun getMessage(deviceToken: String, notification: Notification, data: Map<String, String>): Message {
		return Message.builder()
			.setToken(deviceToken)
			.setNotification(notification)
			.putAllData(data)
			.build()
	}

	private fun getNotification(title: String, body: String): Notification {
		return Notification.builder()
			.setTitle(title)
			.setBody(body)
			.build()
	}
}
