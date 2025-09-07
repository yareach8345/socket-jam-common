package com.yareach.socketjamcommon.domain.security

import com.yareach.socketjamcommon.util.JwtUtil
import com.yareach.socketjamcommon.vo.user.UserVo
import io.jsonwebtoken.Jwts
import java.security.Key
import java.security.interfaces.RSAPrivateKey
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

class JwtTokenEncoder(
    private val secretKey: Key,
    private val expiredMs: Long
) {
    companion object {
        fun fromSecretKey(
            secretKey: SecretKey,
            expiredMs: Long = 24 * 60 * 60 * 1000,
        ): JwtTokenEncoder {
//            val secretKey = SecretKeySpec(
//                secretKeyString.toByteArray(),
//                Jwts.SIG.HS256.key().build().algorithm
//            )
//
            return JwtTokenEncoder(secretKey, expiredMs)
        }

        fun fromPrivateKey(
            privateKey: RSAPrivateKey,
            expiredMs: Long = 24 * 60 * 60 * 1000,
        ): JwtTokenEncoder {
//            val privateKey = JwtUtil.stringToPrivateKey(privateKeyString)

            return JwtTokenEncoder(privateKey, expiredMs)
        }
    }

    fun createJwt(nickName: String, userId: UUID): String {
        return Jwts.builder()
            .claim("nickName", nickName)
            .claim("userId", userId.toString())
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + expiredMs))
            .signWith(secretKey)
            .compact()
    }

    fun createJwt(userVo: UserVo): String = createJwt(userVo.nickName, userVo.userId)
}