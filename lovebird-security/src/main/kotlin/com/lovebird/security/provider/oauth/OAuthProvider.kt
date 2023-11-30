package com.lovebird.security.provider.oauth

import com.lovebird.security.dto.param.OAuthParam

interface OAuthProvider {
	fun getProviderId(request: Any): String
	fun getOAuthParam(request: Any): OAuthParam
}
