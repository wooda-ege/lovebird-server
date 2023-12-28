description = "fcm module"

dependencies {
	implementation("org.springframework:spring-context:6.1.1")
	compileOnly("jakarta.platform:jakarta.jakartaee-api:10.0.0")

	implementation("com.google.firebase:firebase-admin:9.2.0")

	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${property("coroutinesVersion")}")
	runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${property("coroutinesVersion")}")
	runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:${property("coroutinesVersion")}")
}
