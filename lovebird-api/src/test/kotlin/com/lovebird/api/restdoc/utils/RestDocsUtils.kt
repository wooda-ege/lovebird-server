package com.lovebird.api.restdoc.utils

import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.snippet.Snippet
import org.springframework.test.web.servlet.ResultActions


fun ResultActions.andDocument(
	identifier: String,
	vararg snippets: Snippet
): ResultActions {
	return andDo(document(identifier, *snippets))
}
