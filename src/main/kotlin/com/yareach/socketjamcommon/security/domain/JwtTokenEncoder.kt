package com.yareach.socketjamcommon.security.domain

import io.jsonwebtoken.Jwts
import java.security.Key
import java.security.interfaces.RSAPrivateKey
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

class JwtTokenEncoder(
    private val secretKey: Key,
    private val expiredMs: Long
): TokenEncoder {
    override fun createJwt(userId: UUID): String {
        return Jwts.builder()
            .claim("userId", userId.toString())
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + expiredMs))
            .signWith(secretKey)
            .compact()
    }
}

fun TokenEncoder (
    secretKey: SecretKey,
    expiredMs: Long = 24 * 60 * 60 * 1000,
): TokenEncoder {
    return JwtTokenEncoder(secretKey, expiredMs)
}

fun TokenEncoder(
    privateKey: RSAPrivateKey,
    expiredMs: Long = 24 * 60 * 60 * 1000,
): TokenEncoder {

    return JwtTokenEncoder(privateKey, expiredMs)
}