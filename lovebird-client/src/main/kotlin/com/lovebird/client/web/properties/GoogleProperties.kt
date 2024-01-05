package com.lovebird.client.web.properties

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class GoogleProperties(
	@Value("\${oauth.google.client-id}")
	private val clientId: String
) {

	@Bean
	fun googleIdTokenVerifier(): GoogleIdTokenVerifier {
		return GoogleIdTokenVerifier
			.Builder(NetHttpTransport(), GsonFactory())
			.setAudience(Collections.singletonList(clientId))
			.build()
	}
}
