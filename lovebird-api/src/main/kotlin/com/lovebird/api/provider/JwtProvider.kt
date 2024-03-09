package com.lovebird.api.provider

import com.lovebird.api.vo.JwtToken
import com.lovebird.api.vo.PrincipalUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date

@Component
class JwtProvider(
	@Value("\${jwt.expiration.access-token}")
	private val accessTokenExpiredSecond: Long,
	@Value("\${jwt.expiration.access-token}")
	private val refreshTokenExpiredSecond: Long,
	@Value("\${jwt.grant-type}")
	private val grantType: String,
	private val key: Key
) {

	fun generateJwtToken(principalUser: PrincipalUser): JwtToken {
		val claims = getClaims(principalUser)
		val accessToken = generateAccessToken(principalUser, claims)
		val refreshToken = generateRefreshToken(principalUser, claims)

		return JwtToken(accessToken, refreshToken, grantType)
	}

	private fun getClaims(principalUser: PrincipalUser): Claims {
		val claims = Jwts.claims()
		claims["id"] = principalUser.name
		return claims
	}

	private fun generateAccessToken(principalUser: PrincipalUser, claims: Claims): String {
		return getToken(principalUser, claims, accessTokenExpiredSecond)
	}

	private fun generateRefreshToken(principalUser: PrincipalUser, claims: Claims): String {
		return getToken(principalUser, claims, refreshTokenExpiredSecond)
	}

	private fun getToken(principalUser: PrincipalUser, claims: Claims, validationSecond: Long): String {
		return Jwts.builder()
			.setSubject(principalUser.name)
			.setClaims(claims)
			.signWith(key, SignatureAlgorithm.HS512)
			.setExpiration(Date(System.currentTimeMillis() + validationSecond))
			.compact()
	}
}
