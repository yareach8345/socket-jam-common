package com.yareach.socketjamcommon.config.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import kotlin.collections.map

class CustomUserDetail(
    val userId: String,
    val nickName: String,
    val role: List<String>
): UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return role.map { SimpleGrantedAuthority(it) }
    }

    override fun getPassword(): String {
        return ""
    }

    override fun getUsername(): String {
        return userId
    }
}