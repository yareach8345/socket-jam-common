package com.yareach.socketjamcommon.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import kotlin.takeIf
import kotlin.text.toByteArray

class JwtUtils(
    secretSecret: String,
    private val expiredMs: Long = 24 * 60 * 60 * 1000,
) {
   private val secretKey: SecretKey = SecretKeySpec(
       secretSecret.toByteArray(),
       Jwts.SIG.HS256.key().build().algorithm
   )

    fun getPayload(token: String): Claims =
        Jwts
            .parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload

    fun getUserName(token: String) = getPayload(token)["userName"]
        .takeIf { it is String } as String? ?: throw kotlin.IllegalArgumentException("userName is must be String")

    fun getUserId(token: String) = getPayload(token)["userId"]
        .takeIf { it is String } as String? ?: throw kotlin.IllegalArgumentException("userId is must be String")

    fun isValidToken(token: String): Boolean = Jwts.parser().verifyWith(secretKey).build().isSigned(token)

    fun createJwt(userName: String, userId: UUID): String {
        return Jwts.builder()
            .claim("userName", userName)
            .claim("userId", userId.toString())
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + expiredMs))
            .signWith(secretKey)
            .compact()
    }
}