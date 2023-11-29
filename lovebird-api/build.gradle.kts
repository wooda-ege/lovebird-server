description = "api module"

tasks.getByName("bootJar") {
	enabled = true
}

tasks.getByName("jar") {
	enabled = false
}

dependencies {
	implementation(project(":lovebird-common"))
	implementation(project(":lovebird-domain"))
	implementation(project(":lovebird-external:s3"))
	implementation(project(":lovebird-infra:logging"))
	implementation(project(":lovebird-infra:monitoring"))

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
}
