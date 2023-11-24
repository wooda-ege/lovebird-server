import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
	kotlin("kapt")
	kotlin("plugin.spring") apply false
	kotlin("plugin.jpa") apply false
	id("org.springframework.boot") apply false
	id("io.spring.dependency-management")
}

group = "com.lovebird"
version = "1.0.0-SNAPSHOT"

java.sourceCompatibility = JavaVersion.valueOf("VERSION_${property("javaVersion")}")

allprojects {
	group = "${property("projectGroup")}"
	version = "${property("applicationVersion")}"

	repositories {
		mavenCentral()
	}
}

subprojects {
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "org.jetbrains.kotlin.kapt")
	apply(plugin = "org.jetbrains.kotlin.plugin.spring")
	apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	dependencies {
		implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
		implementation("org.jetbrains.kotlin:kotlin-reflect")
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "19"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}

	tasks.getByName("bootJar") {
		enabled = false
	}

	tasks.getByName("jar") {
		enabled = true
	}
}
