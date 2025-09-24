package com.yareach.socketjamcommon.security.domain

import com.yareach.socketjamcommon.user.model.UserIdentify
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import java.security.interfaces.RSAPublicKey
import java.util.UUID
import javax.crypto.SecretKey

class JwtTokenDecoder(
    private val decodingKey: DecodingKey,
): TokenDecoder {
    override fun getPayload(token: String): Claims =
        Jwts
            .parser()
            .let(decodingKey::verify)
            .build()
            .parseSignedClaims(token)
            .payload

    override fun getUserId(token: String) = getPayload(token)["userId"]
        .takeIf { it is String } as String? ?: throw kotlin.IllegalArgumentException("userId is must be String")

    override fun isValidToken(token: String): Boolean = Jwts.parser().let(decodingKey::verify).build().isSigned(token)

    override fun getUserIdentify(token: String): UserIdentify {
        val userId = getUserId(token)

        return UserIdentify(UUID.fromString(userId))
    }
}

fun TokenDecoder(secretKey: SecretKey): TokenDecoder {
    return JwtTokenDecoder(UseSecretKey(secretKey))
}

fun TokenDecoder(publicKey: RSAPublicKey): TokenDecoder {
    return JwtTokenDecoder(UsePublicKey(publicKey))
}