package com.yareach.socketjamcommon.config.security

import com.yareach.socketjamcommon.domain.security.JwtTokenDecoder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class JwtAuthenticationConverter(
    private val jwtUtils: JwtTokenDecoder
) {
    fun convert(authHeader: String): UsernamePasswordAuthenticationToken? {
        val token = authHeader
            .takeIf { it.startsWith("Bearer ") }
            ?.substringAfter("Bearer ")
            ?.takeIf { it.isNotBlank() }

        if(token == null || !jwtUtils.isValidToken(token)) {
            return null
        }

        val nickName = jwtUtils.getNickName(token)

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