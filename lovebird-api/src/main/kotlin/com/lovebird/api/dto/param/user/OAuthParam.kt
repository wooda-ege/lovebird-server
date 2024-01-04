package com.lovebird.api.dto.param.user

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import io.jsonwebtoken.Claims

data class OAuthParam(
	val providerId: String,
	val email: String
) {
	companion object {
		fun from(userInfo: Map<String, String>): OAuthParam {
			return OAuthParam(userInfo["id"]!!, userInfo["email"]!!)
		}

		fun from(claims: Claims): OAuthParam {
			return OAuthParam(claims.subject, claims["email"] as String)
		}

		fun from(googleIdToken: GoogleIdToken): OAuthParam {
			return OAuthParam(googleIdToken.payload.subject, googleIdToken.payload.email)
		}
	}
}
