package com.lovebird.security.provider.oauth

import com.lovebird.webClient.client.NaverAuthClient
import com.lovebird.webClient.client.dto.request.NaverLoginClientRequest
import com.lovebird.webClient.client.dto.request.NaverUserInfoClientRequest
import com.lovebird.webClient.client.dto.response.NaverUserInfoResponse
import com.lovebird.security.dto.param.OAuthParam
import org.springframework.stereotype.Component

@Component
class NaverAuthProvider(
	private val naverAuthClient: NaverAuthClient
) : OAuthProvider {

	override fun getProviderId(request: Any): String {
		return getOAuthParam(request as NaverLoginClientRequest).providerId
	}

	override fun getOAuthParam(request: Any): OAuthParam {
		val accessToken = getAccessToken(request as NaverLoginClientRequest)
		val userInfoRequest = NaverUserInfoClientRequest(accessToken)

		return OAuthParam.of(getUserInfo(userInfoRequest))
	}

	private fun getUserInfo(request: NaverUserInfoClientRequest): NaverUserInfoResponse {
		return naverAuthClient.getUserInfo(request).response
	}

	private fun getAccessToken(request: NaverLoginClientRequest): String {
		return naverAuthClient.getAccessToken(request).accessToken
	}
}
