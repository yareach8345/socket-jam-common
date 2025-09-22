package com.yareach.socketjamcommon.config.security

import com.yareach.socketjamcommon.security.domain.TokenDecoder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import java.util.UUID

class JwtAuthenticationConverter(
    private val jwtUtils: TokenDecoder
) {
    fun convert(authHeader: String): UsernamePasswordAuthenticationToken? {
        val token = authHeader
            .takeIf { it.startsWith("Bearer ") }
            ?.substringAfter("Bearer ")
            ?.takeIf { it.isNotBlank() }

        if(token == null || !jwtUtils.isValidToken(token)) {
            return null
        }

        val userId = jwtUtils.getUserId(token)

        val role = listOf("USER")

        val customUserDetails = CustomUserDetail(
            userId = UUID.fromString(userId),
            role = role
        )

        return UsernamePasswordAuthenticationToken(
            customUserDetails,
            null,
            customUserDetails.authorities
        )
    }
}