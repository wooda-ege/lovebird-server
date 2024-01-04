rootProject.name = "lovebird"

include(
	"lovebird-api",
	"lovebird-common",
	"lovebird-domain",
	"lovebird-external:s3",
	"lovebird-external:fcm",
	"lovebird-external:web-client",
	"lovebird-infra:logging",
	"lovebird-infra:monitoring"
)

pluginManagement {
	val kotlinVersion: String by settings
	val springBootVersion: String by settings
	val springDependencyManagementVersion: String by settings
	val ktlintVersion: String by settings
	val asciidoctorVersion: String by settings

	resolutionStrategy {
		eachPlugin {
			when (requested.id.id) {
				"org.jetbrains.kotlin.jvm" -> useVersion(kotlinVersion)
				"org.jetbrains.kotlin.kapt" -> useVersion(kotlinVersion)
				"org.jetbrains.kotlin.plugin.jpa" -> useVersion(kotlinVersion)
				"org.jetbrains.kotlin.plugin.spring" -> useVersion(kotlinVersion)
				"org.springframework.boot" -> useVersion(springBootVersion)
				"io.spring.dependency-management" -> useVersion(springDependencyManagementVersion)
				"org.jlleitschuh.gradle.ktlint" -> useVersion(ktlintVersion)
				"org.jlleitschuh.gradle.ktlint-idea" -> useVersion(ktlintVersion)
				"org.asciidoctor.jvm.convert" -> useVersion(asciidoctorVersion)
			}
		}
	}
}
