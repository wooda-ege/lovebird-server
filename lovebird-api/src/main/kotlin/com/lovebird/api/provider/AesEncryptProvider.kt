package com.lovebird.api.provider

import org.springframework.beans.factory.annotation.Value
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AesEncryptProvider {

	@Value("\${aes.secret-key}")
	private lateinit var key: String
	private val encoder = Base64.getEncoder()
	private val decoder = Base64.getDecoder()

	fun encryptString(encryptString: String): String {
		val encryptedString = cipherPkcs5(Cipher.ENCRYPT_MODE).doFinal(encryptString.toByteArray(Charsets.UTF_8))

		return String(encoder.encode(encryptedString))
	}

	fun decryptString(decryptString: String): String {
		val byteString = decoder.decode(decryptString.toByteArray(Charsets.UTF_8))

		return String(cipherPkcs5(Cipher.DECRYPT_MODE).doFinal(byteString))
	}

	private fun cipherPkcs5(opMode: Int): Cipher {
		val c = Cipher.getInstance("AES/CBC/PKCS5Padding")

		c.init(opMode, generateAesSecretKey(), generateIvVector())
		return c
	}

	private fun generateAesSecretKey(): SecretKeySpec {
		return SecretKeySpec(key.toByteArray(Charsets.UTF_8), "AES")
	}

	private fun generateIvVector(): IvParameterSpec {
		return IvParameterSpec(key.substring(0, 16).toByteArray(Charsets.UTF_8))
	}
}
