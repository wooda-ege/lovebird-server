package com.lovebird.api.factory

import com.lovebird.api.dto.param.user.OAuthParam
import com.lovebird.api.provider.oauth.AppleAuthProvider
import com.lovebird.api.provider.oauth.GoogleAuthProvider
import com.lovebird.api.provider.oauth.KakaoAuthProvider
import com.lovebird.api.provider.oauth.NaverAuthProvider
import com.lovebird.api.provider.oauth.OAuthProvider
import com.lovebird.common.enums.Provider
import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthProviderFactory(
	private val appleAuthProvider: AppleAuthProvider,
	private val kakaoAuthProvider: KakaoAuthProvider,
	private val googleAuthProvider: GoogleAuthProvider,
	private val naverAuthProvider: NaverAuthProvider
) {

	private val authProviderMap: MutableMap<Provider, OAuthProvider> = EnumMap(Provider::class.java)

	init {
		initialize()
	}

	private fun initialize() {
		authProviderMap[Provider.APPLE] = appleAuthProvider
		authProviderMap[Provider.KAKAO] = kakaoAuthProvider
		authProviderMap[Provider.GOOGLE] = googleAuthProvider
		authProviderMap[Provider.NAVER] = naverAuthProvider
	}

	fun getProviderId(provider: Provider, request: Any): String {
		return getProvider(provider).getProviderId(request)
	}

	fun getOAuthParam(provider: Provider, request: Any): OAuthParam {
		return getProvider(provider).getOAuthParam(request)
	}

	private fun getProvider(provider: Provider): OAuthProvider {
		return authProviderMap[provider] ?: throw LbException(ReturnCode.WRONG_PROVIDER)
	}
}
