package com.yareach.socketjamcommon.domain.security

import com.yareach.socketjamcommon.vo.user.UserVo
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import java.security.interfaces.RSAPublicKey
import java.util.UUID
import javax.crypto.SecretKey

class JwtTokenDecoder(
    private val decodingKey: DecodingKey,
) {
    companion object {
        fun fromSecretKey(secretKey: SecretKey): JwtTokenDecoder {
//            val secretKey = SecretKeySpec(
//                secretKeyString.toByteArray(),
//                Jwts.SIG.HS256.key().build().algorithm
//            )
            return JwtTokenDecoder(UseSecretKey(secretKey))
        }

        fun fromPublicKey(publicKey: RSAPublicKey): JwtTokenDecoder {
            return JwtTokenDecoder(UsePublicKey(publicKey))
        }
    }

    fun getPayload(token: String): Claims =
        Jwts
            .parser()
            .let(decodingKey::verify)
            .build()
            .parseSignedClaims(token)
            .payload

    fun getNickName(token: String) = getPayload(token)["nickName"]
        .takeIf { it is String } as String? ?: throw kotlin.IllegalArgumentException("userName is must be String")

    fun getUserId(token: String) = getPayload(token)["userId"]
        .takeIf { it is String } as String? ?: throw kotlin.IllegalArgumentException("userId is must be String")

    fun isValidToken(token: String): Boolean = Jwts.parser().let(decodingKey::verify).build().isSigned(token)

    fun getUserVo(token: String): UserVo {
        val nickName = getNickName(token)
        val userId = getUserId(token)

        return UserVo(UUID.fromString(userId), nickName)
    }
}