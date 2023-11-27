description = "s3 module"

dependencies {
	implementation(project(":lovebird-common"))

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.amazonaws:aws-java-sdk-s3:1.12.566")
}
