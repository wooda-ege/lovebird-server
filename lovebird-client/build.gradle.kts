description = "web client module"

dependencies {
	implementation(project(":lovebird-common"))

	implementation("org.springframework.boot:spring-boot-starter-webflux:3.1.2")
	implementation("com.google.api-client:google-api-client-jackson2:2.2.0")
	implementation("com.google.api-client:google-api-client:2.2.0")
}
