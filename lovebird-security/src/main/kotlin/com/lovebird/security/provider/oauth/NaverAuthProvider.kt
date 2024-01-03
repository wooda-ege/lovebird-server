package com.lovebird.security.provider.oauth

import com.lovebird.security.dto.param.NaverLoginParam
import com.lovebird.security.dto.param.OAuthParam
import com.lovebird.webClient.client.NaverAuthClient
import com.lovebird.webClient.client.dto.request.NaverUserInfoClientRequest
import com.lovebird.webClient.client.dto.response.NaverUserInfoResponse
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
