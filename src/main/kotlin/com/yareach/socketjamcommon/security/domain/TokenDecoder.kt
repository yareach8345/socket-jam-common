package com.yareach.socketjamcommon.security.domain

import com.yareach.socketjamcommon.user.dto.UserAuthDto
import io.jsonwebtoken.Claims

interface TokenDecoder {
    fun getPayload(token: String): Claims

    fun getUserId(token: String): String

    fun isValidToken(token: String): Boolean

    fun getUserDto(token: String): UserAuthDto
}