package com.lovebird.api.utils.restdocs

import org.springframework.restdocs.headers.HeaderDescriptor
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.headers.RequestHeadersSnippet

class RestDocsHeader(
	val descriptor: HeaderDescriptor
)

infix fun String.headerMeans(
	description: String
): RestDocsHeader {
	return createField(this, description)
}

private fun createField(
	value: String,
	description: String,
	optional: Boolean = false
): RestDocsHeader {
	val descriptor = HeaderDocumentation
		.headerWithName(value)
		.description(description)

	if (optional) descriptor.optional()

	return RestDocsHeader(descriptor)
}

fun requestHeaders(vararg params: RestDocsHeader): RequestHeadersSnippet {
	return HeaderDocumentation.requestHeaders(params.map { it.descriptor })
}
