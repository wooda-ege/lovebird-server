package com.lovebird.api.validator

import com.fasterxml.jackson.databind.ObjectMapper
import com.lovebird.api.vo.PrincipalUser
import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.reader.UserReader
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.Key
import java.security.PublicKey
import java.util.Base64

@Component
class JwtValidator(
	private val key: Key,
	private val userReader: UserReader
) {
	fun getAuthentication(token: String): Authentication {
		val claims = getTokenClaims(token)
		val user: User = userReader.findEntityById(claims.get("id", String::class.java).toLong())
		val principalUser: PrincipalUser = PrincipalUser.from(user)

		return UsernamePasswordAuthenticationToken(principalUser, "", principalUser.authorities)
	}

	fun getPrincipalUser(token: String): PrincipalUser {
		val claims = getTokenClaims(token)
		val user: User = userReader.findEntityById(claims.get("id", String::class.java).toLong())

		return PrincipalUser.from(user)
	}

	@Suppress("UNCHECKED_CAST")
	fun parseHeaders(token: String): Map<String, String> {
		val header = token.split(".")[0]

		return ObjectMapper().readValue(
			decodeHeader(header),
			MutableMap::class.java
		) as Map<String, String>
	}

	fun decodeHeader(token: String): String {
		return String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8)
	}

	fun getTokenClaims(token: String): Claims {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.body
		} catch (e: ExpiredJwtException) {
			throw LbException(ReturnCode.EXPIRED_JWT_TOKEN)
		} catch (e: RuntimeException) {
			throw LbException(ReturnCode.WRONG_JWT_TOKEN)
		}
	}

	fun getTokenClaims(token: String, publicKey: PublicKey): Claims {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(publicKey)
				.build()
				.parseClaimsJws(token)
				.body
		} catch (e: RuntimeException) {
			throw LbException(ReturnCode.WRONG_JWT_TOKEN)
		}
	}
}
