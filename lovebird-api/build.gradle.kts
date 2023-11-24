description = "api module"

tasks.getByName("bootJar") {
	enabled = true
}

tasks.getByName("jar") {
	enabled = false
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
}
