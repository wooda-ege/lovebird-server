description = "logging module"

dependencies {
	implementation("io.micrometer:micrometer-tracing-bridge-brave")
	implementation("io.sentry:sentry-logback:${property("sentryVersion")}")
}
