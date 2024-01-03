package com.lovebird.security.dto.param

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import io.jsonwebtoken.Claims

data class OAuthParam(
	val providerId: String,
	val email: String
) {
	companion object {
		fun from(response: Map<String, String>): OAuthParam {
			return OAuthParam(response["id"]!!, response["email"]!!)
		}

		fun from(claims: Claims): OAuthParam {
			return OAuthParam(claims.subject, claims["email"] as String)
		}

		fun from(googleIdToken: GoogleIdToken): OAuthParam {
			return OAuthParam(googleIdToken.payload.subject, googleIdToken.payload.email)
		}
	}
}
