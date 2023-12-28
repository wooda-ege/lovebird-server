description = "api module"

plugins {
	id("org.asciidoctor.jvm.convert")
}

val asciidoctorExt: Configuration by configurations.creating
val snippetsDir by extra { file("build/generated-snippets") }

dependencies {
	implementation(project(":lovebird-common"))
	implementation(project(":lovebird-domain"))
	implementation(project(":lovebird-security"))
	implementation(project(":lovebird-external:s3"))
	implementation(project(":lovebird-external:fcm"))
	implementation(project(":lovebird-infra:logging"))
	implementation(project(":lovebird-infra:monitoring"))

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")

	asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

	testImplementation("io.mockk:mockk:${property("mockKVersion")}")
	testImplementation("io.kotest:kotest-runner-junit5:${property("kotestVersion")}")
	testImplementation("io.kotest:kotest-assertions-core:${property("kotestVersion")}")
	testImplementation("io.kotest.extensions:kotest-extensions-spring:${property("kotestSpringVersion")}")
	testImplementation("com.ninja-squad:springmockk:4.0.2")
}

tasks.asciidoctor {
	inputs.dir(snippetsDir)
	configurations(asciidoctorExt.name)
	dependsOn(tasks.test)

	sources {
		include("**/index.adoc")
	}

	baseDirFollowsSourceFile()

	doFirst {
		delete {
			file("src/main/resources/static/docs")
		}
	}
}

tasks.register("copyHtml", Copy::class) {
	dependsOn(tasks.asciidoctor)
	from(file("build/docs/asciidoc/"))
	into(file("src/main/resources/static/docs"))
}

tasks.build {
	dependsOn(tasks.getByName("copyHtml"))
}

tasks.bootJar {
	dependsOn(tasks.asciidoctor)
	dependsOn(tasks.getByName("copyHtml"))
}

tasks.getByName("bootJar") {
	enabled = true
}

tasks.getByName("jar") {
	enabled = false
}
