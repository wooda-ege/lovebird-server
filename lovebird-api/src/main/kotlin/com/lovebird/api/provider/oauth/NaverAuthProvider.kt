package com.lovebird.api.provider.oauth

import com.lovebird.api.dto.param.user.NaverLoginParam
import com.lovebird.api.dto.param.user.OAuthParam
import com.lovebird.client.web.NaverAuthClient
import com.lovebird.client.web.dto.request.NaverUserInfoClientRequest
import org.springframework.stereotype.Component

@Component
class NaverAuthProvider(
	private val naverAuthClient: NaverAuthClient
) : OAuthProvider {

	override fun getProviderId(request: Any): String {
		return getOAuthParam(request as NaverLoginParam).providerId
	}

	override fun getOAuthParam(request: Any): OAuthParam {
		val accessToken = getAccessToken(request as NaverLoginParam)
		val userInfoRequest = NaverUserInfoClientRequest(accessToken)

		return OAuthParam.from(getUserInfo(userInfoRequest))
	}

	private fun getAccessToken(request: NaverLoginParam): String {
		return naverAuthClient.getAccessToken(request.code, request.state)
	}

	private fun getUserInfo(request: NaverUserInfoClientRequest): Map<String, String> {
		return naverAuthClient.getUserInfo(request).response
	}
}
