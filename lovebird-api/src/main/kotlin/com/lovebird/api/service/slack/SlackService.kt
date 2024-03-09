package com.lovebird.api.service.slack

import jakarta.servlet.http.HttpServletRequest
import net.gpedro.integrations.slack.SlackApi
import net.gpedro.integrations.slack.SlackAttachment
import net.gpedro.integrations.slack.SlackField
import net.gpedro.integrations.slack.SlackMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SlackService(
	private val slackApi: SlackApi,
	private val environment: Environment,
	@Value("\${slack.webhook.is-enable}")
	private val isEnable: Boolean
) {

	fun sendSlackForError(exception: Exception, request: HttpServletRequest) {
		if (!isEnable) {
			return
		}
		val slackAttachment = SlackAttachment()
		slackAttachment.setFallback("Error")
		slackAttachment.setColor("danger")
		slackAttachment.setTitle("Error Detect")
		slackAttachment.setTitleLink(request.contextPath)
		slackAttachment.setText(exception.stackTraceToString())
		slackAttachment.setFields(
			listOf(
				SlackField().setTitle("Request URL").setValue(request.requestURL.toString()),
				SlackField().setTitle("Request Method").setValue(request.method),
				SlackField().setTitle("Request Parameter").setValue(getRequestParameters(request)),
				SlackField().setTitle("Request Time").setValue(LocalDateTime.now().toString()),
				SlackField().setTitle("Request IP").setValue(request.remoteAddr),
				SlackField().setTitle("Request User-Agent").setValue(request.getHeader("User-Agent"))
			)
		)
		val profile = environment.getProperty("spring.profiles.default")
		val slackMessage = SlackMessage()

		slackMessage.setAttachments(listOf(slackAttachment))
		slackMessage.setChannel("#error-log")
		slackMessage.setUsername("$profile API Error")
		slackMessage.setIcon(":alert:")
		slackMessage.setText("$profile api 에러 발생")

		slackApi.call(slackMessage)
	}

	private fun getRequestParameters(request: HttpServletRequest): String {
		val parameterMap = request.parameterMap
		val sb = StringBuilder()
		parameterMap.forEach { (key, value) ->
			sb.append("$key: ${value.joinToString(", ")}\n")
		}
		return sb.toString()
	}
}
