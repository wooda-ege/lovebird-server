description = "api module"

plugins {
	id("jacoco")
	id("org.asciidoctor.jvm.convert")
}

val asciidoctorExt: Configuration by configurations.creating
val snippetsDir by extra { file("build/generated-snippets") }

dependencies {
	implementation(project(":lovebird-common"))
	implementation(project(":lovebird-domain"))
	implementation(project(":lovebird-client"))
	implementation(project(":lovebird-external:s3"))
	implementation(project(":lovebird-external:fcm"))
	implementation(project(":lovebird-infra:logging"))
	implementation(project(":lovebird-infra:monitoring"))

	// Basic
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework:spring-tx")

	// Security & OAuth
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("com.google.api-client:google-api-client-jackson2:2.2.0")
	implementation("com.google.api-client:google-api-client:2.2.0")

	// Jwt
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

	// Test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")

	// Spring Rest Docs
	asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

	// Kotest & Mockk
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

/**
 * Jacoco
 */
tasks.withType<JacocoReport> {
	reports {
		html.required.set(true)
		xml.required.set(false)
		csv.required.set(false)
	}

	finalizedBy("jacocoTestCoverageVerification")
}

tasks.withType<JacocoCoverageVerification> {
	violationRules {
		rule {
			enabled = true
			element = "CLASS"

			limit {
				counter = "LINE"
				value = "COVEREDRATIO"
				minimum = "0.00".toBigDecimal()
			}

			limit {
				counter = "LINE"
				value = "TOTALCOUNT"
				maximum = "200".toBigDecimal()
			}

			excludes = listOf()
		}
	}
}

val testCoverage by tasks.registering {
	group = "verification"
	description = "Runs the unit tests with coverage"

	dependsOn(
		":test",
		":jacocoTestReport",
		":jacocoTestCoverageVerification"
	)

	tasks["jacocoTestReport"].mustRunAfter(tasks["test"])
	tasks["jacocoTestCoverageVerification"].mustRunAfter(tasks["jacocoTestReport"])
}

tasks.test {
	extensions.configure(JacocoTaskExtension::class) {
		destinationFile = file("$buildDir/jacoco/jacoco.exec")
	}
	outputs.dir(snippetsDir)
	finalizedBy("jacocoTestReport")
}

tasks.getByName("bootJar") {
	enabled = true
}

tasks.getByName("jar") {
	enabled = false
}
