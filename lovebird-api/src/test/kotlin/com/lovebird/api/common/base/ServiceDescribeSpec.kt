package com.lovebird.api.common.base

import io.kotest.core.spec.style.DescribeSpec
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito

@ExtendWith(MockKExtension::class)
abstract class ServiceDescribeSpec(
	body: DescribeSpec.() -> Unit = {}
) : DescribeSpec(body) {

	companion object {
		fun <T> any(type: Class<T>): T = Mockito.any(type)
	}
}
