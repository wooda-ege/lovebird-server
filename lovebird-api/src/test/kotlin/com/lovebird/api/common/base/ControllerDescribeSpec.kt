package com.lovebird.api.common.base

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.lovebird.api.config.WebMvcConfig
import com.lovebird.api.utils.restdocs.OBJECT
import com.lovebird.api.utils.restdocs.RestDocsField
import com.lovebird.api.utils.restdocs.STRING
import com.lovebird.api.utils.restdocs.responseBody
import com.lovebird.api.utils.restdocs.type
import com.lovebird.api.validator.JwtValidator
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.context.annotation.Import
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.payload.ResponseFieldsSnippet

@Import(WebMvcConfig::class)
@ExtendWith(RestDocumentationExtension::class)
abstract class ControllerDescribeSpec(
	body: DescribeSpec.() -> Unit = {}
) : DescribeSpec(body) {

	@MockkBean
	protected lateinit var jwtValidator: JwtValidator

	companion object {
		private val mapper: ObjectMapper = ObjectMapper()
			.registerModule(JavaTimeModule())
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

		fun toJson(value: Any): String {
			return mapper.writeValueAsString(value)
		}

		fun <T> any(type: Class<T>): T = Mockito.any(type)

		fun successResponseBody(vararg fields: RestDocsField, dataOptional: Boolean = false): ResponseFieldsSnippet {
			return responseBody(
				"code" type STRING means "응답 코드",
				"message" type STRING means "응답 메시지",
				"data" type OBJECT means "응답 데이터" isOptional dataOptional,
			).and(fields.map { it.descriptor })
		}
	}
}
