description = "fcm module"

dependencies {
	implementation("org.springframework:spring-context:6.1.1")
	compileOnly("jakarta.platform:jakarta.jakartaee-api:10.0.0")

	implementation("com.google.firebase:firebase-admin:9.2.0")

	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${property("coroutinesVersion")}")
	runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${property("coroutinesVersion")}")
	runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:${property("coroutinesVersion")}")

	implementation("org.springframework.boot:spring-boot-starter-log4j2")
}

configurations.forEach {
	it.exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
	it.exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
}
