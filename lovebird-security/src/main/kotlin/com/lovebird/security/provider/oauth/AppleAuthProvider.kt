package com.lovebird.security.provider.oauth

import com.lovebird.security.dto.param.OAuthParam
import com.lovebird.security.provider.key.PublicKeyProvider
import com.lovebird.security.validator.JwtValidator
import com.lovebird.webClient.client.AppleAuthClient
import io.jsonwebtoken.Claims
import org.springframework.stereotype.Component

@Component
class AppleAuthProvider(
	private val appleAuthClient: AppleAuthClient,
	private val publicKeyProvider: PublicKeyProvider,
	private val jwtValidator: JwtValidator
) : OAuthProvider {

	override fun getProviderId(request: Any): String {
		return getClaims(idToken = request as String).subject
	}

	override fun getOAuthParam(request: Any): OAuthParam {
		return OAuthParam.of(getClaims(idToken = request as String))
	}

	private fun getClaims(idToken: String): Claims {
		val publicKey = publicKeyProvider.generatePublicKey(getHeaders(idToken), appleAuthClient.getPublicKeys())
		return jwtValidator.getTokenClaims(idToken, publicKey)
	}

	private fun getHeaders(idToken: String): Map<String, String> {
		return jwtValidator.parseHeaders(idToken)
	}
}
