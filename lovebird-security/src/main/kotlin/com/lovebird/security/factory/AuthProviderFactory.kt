package com.lovebird.security.factory

import com.lovebird.common.enums.Provider
import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.lovebird.security.dto.param.OAuthParam
import com.lovebird.security.provider.oauth.AppleAuthProvider
import com.lovebird.security.provider.oauth.GoogleAuthProvider
import com.lovebird.security.provider.oauth.KakaoAuthProvider
import com.lovebird.security.provider.oauth.NaverAuthProvider
import com.lovebird.security.provider.oauth.OAuthProvider
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
