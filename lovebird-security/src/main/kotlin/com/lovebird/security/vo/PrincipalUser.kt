package com.lovebird.security.vo

import com.lovebird.domain.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.user.OAuth2User

class PrincipalUser(
	private val user: User,
	private val attribute: MutableMap<String, Any>
) : UserDetails, OidcUser, OAuth2User {

	fun getUser(): User = user

	override fun getName(): String = user.id.toString()

	override fun getAttributes(): MutableMap<String, Any> = attribute

	override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
		mutableListOf(SimpleGrantedAuthority(user.role.toString()))

	override fun getUsername(): String = user.providerId

	override fun getPassword(): String = "password"

	override fun isAccountNonExpired(): Boolean = true

	override fun isAccountNonLocked(): Boolean = true

	override fun isCredentialsNonExpired(): Boolean = true

	override fun isEnabled(): Boolean = true

	override fun getClaims(): MutableMap<String, Any>? = null

	override fun getUserInfo(): OidcUserInfo? = null

	override fun getIdToken(): OidcIdToken? = null

	companion object {
		fun of(user: User): PrincipalUser {
			return PrincipalUser(user, mutableMapOf("id" to user.id!!))
		}
	}
}
