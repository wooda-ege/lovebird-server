package com.lovebird.security.config

import com.lovebird.security.filter.JwtAuthenticationFilter
import com.lovebird.security.filter.JwtExceptionFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
	private val jwtAuthenticationFilter: JwtAuthenticationFilter,
	private val jwtExceptionFilter: JwtExceptionFilter
) {

	@Bean
	fun oauth2SecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
		return http
			.httpBasic { basic -> basic.disable() }
			.csrf { csrf -> csrf.disable() }
			.formLogin { form -> form.disable() }
			.headers { header -> header.frameOptions { frameOptions -> frameOptions.disable() } }
			.sessionManagement { setSessionManagement() }
			.authorizeHttpRequests(setAuthorizePath())
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
			.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter::class.java)
			.build()
	}

	private fun setSessionManagement(): Customizer<SessionManagementConfigurer<HttpSecurity>> {
		return Customizer { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
	}

	private fun setAuthorizePath(): Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> {
		return Customizer {
			it
				.requestMatchers(AntPathRequestMatcher("/docs/**", "/api/v1/auth/**")).permitAll()
				.requestMatchers("api/v1/calendar/**").permitAll()
				//.requestMatchers("/api/v1/**").hasAnyRole("USER", "ADMIN")
				.anyRequest().authenticated()
		}
	}
}
