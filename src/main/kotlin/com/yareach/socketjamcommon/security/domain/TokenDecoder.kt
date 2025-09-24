package com.yareach.socketjamcommon.security.domain

import com.yareach.socketjamcommon.user.model.UserIdentify
import io.jsonwebtoken.Claims

interface TokenDecoder {
    fun getPayload(token: String): Claims

    fun getUserId(token: String): String

    fun isValidToken(token: String): Boolean

    fun getUserIdentify(token: String): UserIdentify
}