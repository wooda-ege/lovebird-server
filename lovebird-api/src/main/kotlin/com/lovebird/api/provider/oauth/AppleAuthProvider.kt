package com.lovebird.api.provider.oauth

import com.lovebird.api.dto.param.user.OAuthParam
import com.lovebird.api.provider.PublicKeyProvider
import com.lovebird.api.validator.JwtValidator
import com.lovebird.client.web.AppleAuthClient
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
		return OAuthParam.from(getClaims(idToken = request as String))
	}

	private fun getClaims(idToken: String): Claims {
		val publicKey = publicKeyProvider.generatePublicKey(getHeaders(idToken), appleAuthClient.getPublicKeys())
		return jwtValidator.getTokenClaims(idToken, publicKey)
	}

	private fun getHeaders(idToken: String): Map<String, String> {
		return jwtValidator.parseHeaders(idToken)
	}
}
