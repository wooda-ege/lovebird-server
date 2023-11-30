package com.lovebird.security.provider.oauth

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.lovebird.security.dto.param.OAuthParam
import org.springframework.stereotype.Component

@Component
class GoogleAuthProvider(
	private val googleIdTokenVerifier: GoogleIdTokenVerifier
) : OAuthProvider {

	override fun getProviderId(request: Any): String {
		return getGoogleIdToken(idToken = request as String).payload.subject
	}

	override fun getOAuthParam(request: Any): OAuthParam {
		return OAuthParam.of(getGoogleIdToken(idToken = request as String))
	}

	private fun getGoogleIdToken(idToken: String): GoogleIdToken {
		return googleIdTokenVerifier.verify(idToken)
	}
}
