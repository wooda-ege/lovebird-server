package com.lovebird.api.provider.oauth

import com.lovebird.api.dto.param.user.OAuthParam

interface OAuthProvider {
	fun getProviderId(request: Any): String
	fun getOAuthParam(request: Any): OAuthParam
}
