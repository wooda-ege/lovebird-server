package com.lovebird.fcm.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource


@Configuration
class FCMConfig(
	@Value("\${firebase.certification}")
	private val adminSdk: String,
	@Value("\${firebase.scope}")
	private val scope: String
) {

	@PostConstruct
	fun firebaseApp() {
		val account = ClassPathResource(adminSdk)
		FirebaseApp.initializeApp(getOptions(account))
	}

	private fun getOptions(config: ClassPathResource): FirebaseOptions {
		return FirebaseOptions.builder()
			.setCredentials(GoogleCredentials.fromStream(config.getInputStream()).createScoped(setOf(scope)))
			.build()
	}
}
