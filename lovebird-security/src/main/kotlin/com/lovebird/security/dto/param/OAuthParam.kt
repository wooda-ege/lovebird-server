package com.lovebird.security.dto.param

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.lovebird.webClient.client.dto.response.NaverUserInfoResponse
import io.jsonwebtoken.Claims

data class OAuthParam(
	val providerId: String,
	val email: String
) {
	companion object {
		fun of(response: NaverUserInfoResponse): OAuthParam {
			return OAuthParam(response.id, response.email)
		}

		fun of(claims: Claims): OAuthParam {
			return OAuthParam(claims.subject, claims["email"] as String)
		}

		fun of(googleIdToken: GoogleIdToken): OAuthParam {
			return OAuthParam(googleIdToken.payload.subject, googleIdToken.payload.email)
		}
	}
}
