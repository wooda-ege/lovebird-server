description = "security module"

dependencies {
	implementation(project(":lovebird-common"))
	implementation(project(":lovebird-domain"))
	implementation(project(":lovebird-external:web-client"))

	implementation("jakarta.servlet:jakarta.servlet-api:5.0.0")

	implementation("org.springframework.boot:spring-boot-starter-security")
	api("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("com.google.api-client:google-api-client-jackson2:2.2.0")
	implementation("com.google.api-client:google-api-client:2.2.0")

	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
}
