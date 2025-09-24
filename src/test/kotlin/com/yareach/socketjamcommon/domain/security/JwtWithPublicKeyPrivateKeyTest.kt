package com.yareach.socketjamcommon.domain.security

import com.yareach.socketjamcommon.user.model.UserIdentify
import com.yareach.socketjamcommon.security.domain.TokenDecoder
import com.yareach.socketjamcommon.security.domain.TokenEncoder
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.SignatureException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.Date
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class JwtWithPublicKeyPrivateKeyTest {
    private val keyPair = KeyPairGenerator
        .getInstance("RSA")
        .apply { initialize(2048) }
        .generateKeyPair()

    private val privateKey = keyPair.private
    private val publicKey = keyPair.public

    private val jwtEncoder = TokenEncoder( privateKey as RSAPrivateKey )
    private val jwtDecoder = TokenDecoder( publicKey as RSAPublicKey )

    val testUser = UserIdentify(UUID.randomUUID())

    @Test
    @DisplayName("Token 생성 테스트")
    fun generateToken() {
        val jwt = jwtEncoder.createJwt(testUser.userId)

        assertTrue(jwtDecoder.isValidToken(jwt))

        assertEquals(testUser.userId.toString(), jwtDecoder.getUserId(jwt))

        val userVo = jwtDecoder.getUserIdentify(jwt)

        assertEquals(testUser.userId, userVo.userId)
    }


    @Test
    @DisplayName("UserVo로 token 생성")
    fun generateTokenWithUserVo() {
        val jwt = jwtEncoder.createJwt(testUser)

        assertTrue(jwtDecoder.isValidToken(jwt))

        assertEquals(testUser.userId.toString(), jwtDecoder.getUserId(jwt))

        val userVo = jwtDecoder.getUserIdentify(jwt)

        assertEquals(testUser.userId, userVo.userId)
    }

    @Test
    @DisplayName("올바른 secret key로만 파싱 가능")
    fun parsingTokenWithRightSecretKeyType() {
        val token = jwtEncoder.createJwt(testUser)

        assertTrue { Jwts.parser().verifyWith(publicKey).build().isSigned(token) }
        assertEquals(
            testUser.userId.toString(),
            Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token).payload["userId"]
        )
    }

    @Test
    @DisplayName("잘못된 secret key를 사용하면 실패")
    fun parsingTokenWithWrongSecretKey() {
        val wrongPublicKey = KeyPairGenerator
            .getInstance("RSA")
            .apply { initialize(2048) }
            .generateKeyPair()
            .public

        val token = jwtEncoder.createJwt(testUser)

        assertFailsWith(SignatureException::class) {
            Jwts.parser().verifyWith(wrongPublicKey).build().parseSignedClaims(token)
        }
    }

    @Test
    @DisplayName("payload의 value값이 string이 아니면 실패")
    fun parsingTokenWithWrongPayloadValueType() {
        val token = Jwts.builder()
            .claim("userName", 12345) // string이 아닌 Int 사용
            .claim("userId", 12345) // string이 아닌 Int 사용
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + 1000 * 60))
            .signWith(privateKey)
            .compact()

        assertFailsWith(IllegalArgumentException::class) { jwtDecoder.getUserId(token) }
    }
}