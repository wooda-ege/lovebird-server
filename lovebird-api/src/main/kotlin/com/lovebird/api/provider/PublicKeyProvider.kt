package com.lovebird.api.provider

import com.lovebird.webClient.vo.key.OidcPublicKey
import com.lovebird.webClient.vo.key.OidcPublicKeyList
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec
import java.util.Base64

@Component
class PublicKeyProvider {

	fun generatePublicKey(tokenHeaders: Map<String, String>, applePublicKeys: OidcPublicKeyList): PublicKey {
		val oidcPublicKey: OidcPublicKey = applePublicKeys.getMatchedKey(
			tokenHeaders["kid"],
			tokenHeaders["alg"]
		)

		return getPublicKey(oidcPublicKey)
	}

	private fun getPublicKey(publicKey: OidcPublicKey): PublicKey {
		val publicKeySpec = RSAPublicKeySpec(
			BigInteger(1, decodeBase64(publicKey.n)),
			BigInteger(1, decodeBase64(publicKey.e))
		)

		return KeyFactory.getInstance(publicKey.kty).generatePublic(publicKeySpec)
	}

	private fun decodeBase64(base64String: String): ByteArray {
		return Base64.getDecoder().decode(base64String)
	}
}
