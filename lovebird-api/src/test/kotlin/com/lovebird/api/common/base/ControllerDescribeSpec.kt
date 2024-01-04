package com.lovebird.api.common.base

import com.fasterxml.jackson.databind.ObjectMapper
import com.lovebird.api.config.WebMvcConfig
import com.lovebird.api.controller.external.PresignedUrlController
import com.lovebird.api.validator.JwtValidator
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.restdocs.RestDocumentationExtension

@Import(WebMvcConfig::class)
@ExtendWith(RestDocumentationExtension::class)
@WebMvcTest(
	PresignedUrlController::class
)
abstract class ControllerDescribeSpec(
	body: DescribeSpec.() -> Unit = {}
) : DescribeSpec(body) {

	@MockkBean
	protected lateinit var jwtValidator: JwtValidator

	companion object {
		private val mapper: ObjectMapper = ObjectMapper()

		fun toJson(value: Any): String {
			return mapper.writeValueAsString(value)
		}

		fun <T> any(type: Class<T>): T = Mockito.any(type)
	}
}
