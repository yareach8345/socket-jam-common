package com.yareach.socketjamcommon.config.security

import com.yareach.socketjamcommon.util.JwtUtils
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class JwtAuthenticationConverter(
    private val jwtUtils: JwtUtils
) {
    fun convert(authHeader: String): UsernamePasswordAuthenticationToken? {
        val token = authHeader
            .takeIf { it.startsWith("Bearer ") }
            ?.substringAfter("Bearer ")
            ?.takeIf { it.isNotBlank() }

        if(token == null || !jwtUtils.isValidToken(token)) {
            return null
        }

        val nickName = jwtUtils.getUserName(token)

        val userId = jwtUtils.getUserId(token)

        val role = listOf("USER")

        val customUserDetails = CustomUserDetail(userId, nickName, role)

        return UsernamePasswordAuthenticationToken(
            customUserDetails,
            null,
            customUserDetails.authorities
        )
    }
}