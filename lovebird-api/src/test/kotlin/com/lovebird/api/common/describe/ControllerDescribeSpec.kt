package com.lovebird.api.common.describe

import com.fasterxml.jackson.databind.ObjectMapper
import com.lovebird.api.controller.external.PresignedUrlController
import com.lovebird.api.utils.restdocs.restDocMockMvcBuild
import io.kotest.core.spec.style.DescribeSpec
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext

@WebMvcTest(
	PresignedUrlController::class
)
abstract class ControllerDescribeSpec(
	@Autowired
	private val context: WebApplicationContext,
	body: DescribeSpec.() -> Unit = {}
) : DescribeSpec(body) {

	@Autowired
	protected val mapper: ObjectMapper

	@Autowired
	protected val mockMvc: MockMvc

	protected val restDocumentation: ManualRestDocumentation = ManualRestDocumentation()

	init {
		mockMvc = restDocMockMvcBuild(context, restDocumentation)
		mapper = ObjectMapper()
	}

	protected fun toJson(value: Any): String {
		return mapper.writeValueAsString(value)
	}

	protected fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
}
