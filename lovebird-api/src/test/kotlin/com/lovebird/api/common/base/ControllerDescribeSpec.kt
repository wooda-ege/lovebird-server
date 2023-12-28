package com.lovebird.api.common.describe

import com.fasterxml.jackson.databind.ObjectMapper
import com.lovebird.api.config.WebMvcConfig
import com.lovebird.api.controller.external.PresignedUrlController
import com.lovebird.api.utils.restdocs.restDocMockMvcBuild
import io.kotest.core.spec.BeforeEach
import io.kotest.core.spec.BeforeTest
import io.kotest.core.spec.style.DescribeSpec
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext

@Import(WebMvcConfig::class)
@ExtendWith(RestDocumentationExtension::class)
@WebMvcTest(
	PresignedUrlController::class
)
abstract class ControllerDescribeSpec(
	body: DescribeSpec.() -> Unit = {},
) : DescribeSpec(body) {

	companion object {
		private val mapper: ObjectMapper = ObjectMapper()

		fun toJson(value: Any): String {
			return mapper.writeValueAsString(value)
		}

		fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
	}
}
