package com.yareach.socketjamcommon.utils

import com.yareach.socketjamcommon.domain.security.JwtTokenDecoder
import com.yareach.socketjamcommon.domain.security.JwtTokenEncoder
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertNotNull
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class JwtUtilTest {
    private val ketConverter = KetConverter()

    val publicKeyString = """
        -----BEGIN PUBLIC KEY-----
        MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoZ5rXAQrTd9gt3qhkvMH
        03QgEr18KicVWsMN/py1XA84DEqFlvispaJ5HA1oRXJ950xBrJB7JS9LzesqMFV5
        Y5ZgVTSYRE8qDPfGwcw0e5cMGmNs4+ICLV42DsFvhZz3T161TWxIIUFoQEUAKXx9
        hdhgHsnDwj31/Ro6DgJTE4XVjv7klFluWIln3IwgW47cZEz0BIsyCCz5bC7Q8WmU
        DP/gtprFDFZwY27dfrUsPSbsJhMAFBlspXp40nwqIWar+9lDyDi4mNW9QsNo7mGN
        FnmmZkjYf4++GhslypESYx1vmlXZIsEgjZ6LBYWHBZ6XDfyYZb+A19355DgUj82p
        DQIDAQAB
        -----END PUBLIC KEY-----
    """.trimIndent() + "\n"

    val privateKeyString = """
        -----BEGIN PRIVATE KEY-----
        MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQChnmtcBCtN32C3
        eqGS8wfTdCASvXwqJxVaww3+nLVcDzgMSoWW+KylonkcDWhFcn3nTEGskHslL0vN
        6yowVXljlmBVNJhETyoM98bBzDR7lwwaY2zj4gItXjYOwW+FnPdPXrVNbEghQWhA
        RQApfH2F2GAeycPCPfX9GjoOAlMThdWO/uSUWW5YiWfcjCBbjtxkTPQEizIILPls
        LtDxaZQM/+C2msUMVnBjbt1+tSw9JuwmEwAUGWylenjSfCohZqv72UPIOLiY1b1C
        w2juYY0WeaZmSNh/j74aGyXKkRJjHW+aVdkiwSCNnosFhYcFnpcN/Jhlv4DX3fnk
        OBSPzakNAgMBAAECggEAGG7ZBNMt3MWdWypIjOz9d46BtB499tLBHAngRBKwKZe0
        vcmdAo2GRDrDcRCR0pXpdMiG0S4Sf9+ePSYv0u4eaNt4pXOgZWChFyQflyK7oxzb
        Bo5AuFQYR4A2T0AXuleth6vmCtYI3YYu2tso7XIAOUt/Kd26INO51tFaiiL4fjsq
        gf1IMjq3Rp9Te9U5nOUss4XdwhvGxH+49R83qdQyPiaaVG3z5eDTixTyjFwRY9Tl
        XcNSjQPQ30WPFICUGTkTYvfmguTfUsgeJZVHYMpaczeu5nacr+aGONPMd0a0u1q5
        0L7KZrKnMmG2DnGyNmSg3BZQGFHk+wx88ciHTTRk3QKBgQDjjfHwKwaVIY4NNKpU
        AskxqhPBnCqBX7D9PDMqzGaDwp8iA8ZreCcQY/xa5Lefvkm8gFurjiOqw3/ewJ9r
        Th/FtByUk07rat6Yxu4ut7Hf0IaDbqO+0y+SY/1fodjWXguBNXkXlrdOriG9Z54o
        wLJ+3TIc388Nw2EELVP2ep61dwKBgQC10nF4efUTa5urz4ajBxxJyfYwscVyTXNy
        U071R0Bv4C0+rRpu0pEGemi1rSwtOzhHerP0VbOHAvaw7T2ZRQ6SqlsAj3nn/eCE
        tKh+QxQWhCgwUnaANqHmosM+zllfSbPkQv+FugnJwAtaFqmWpY3ULqRSpb8vUMZO
        njHE03MGmwKBgQDW1R8SQHyxdQF4pD0enZa3RbvrMPXMOLyPCDPCd0FdqfO1GF1O
        /xvaVJtecMqPjQZuabyiJcq0oGfrtG2N0C8c5yOIfd6nUbkgAcfC4XiUNnmYQm9j
        bFxgV/hRPirKj/VKomJVi9w58xo1iEsAbJTYQxrZuc7wToIcgHLho/iMlwKBgQCJ
        cwASqmdpefShqm1Gew6YfZOd9hWkNgarC21qIKYGrPqnIecUlhvKQOk9Rh9aRiVi
        a9D0SYFA9C2XTJE/d6PvndbTkqL+jL6Ys5WYdY8PAnL7LKumeyZjGvHsXcPUM/xI
        95kA98WEfahXP0COwLJ6D5RrYh8oXrC7J/3JpGuVNwKBgQDVtvjcDyOaTO+JqMEF
        xEfYZ9DIQGKhqXzC9P1txDJCh/+LvjFK9bzkaW/+9331PkDWZ1gDrt7qGkQQQyyD
        5el8M9ED34fC6LgElaHVq2JPTXpFU9cnOPEM1Sr3DqxdlRE1/PXnvedCawWPibUE
        tm0OylgIUg98Q85khwvKDJkQvA==
        -----END PRIVATE KEY-----
    """.trimIndent() + "\n"

    val SecretKeyString = "a-string-secret-256-bits-long-for-test"

    @Test
    @DisplayName("public key를 변환하여 decoder 생성")
    fun publicKeyTest() {
        val rsaPublicKey = ketConverter.stringToPublicKey(publicKeyString)

        JwtTokenDecoder.fromPublicKey(rsaPublicKey)

        val publicKeyStr = ketConverter.rsaPublicKeyToString(rsaPublicKey)

        assertEquals(publicKeyString, publicKeyStr)
    }

    @Test
    @DisplayName("private key를 변환하여 encoder 생성")
    fun privateKeyTest() {
        val rsaPrivateKey = ketConverter.stringToPrivateKey(privateKeyString)

        JwtTokenEncoder.fromPrivateKey(rsaPrivateKey)

        val privateKey = ketConverter.rsaPrivateKeyToString(rsaPrivateKey)

        assertEquals(privateKeyString, privateKey)
    }

    @Test
    @DisplayName("secret key를 변환하여 encoder, decoder 생성")
    fun secretKeyTest() {
        val secretKey = ketConverter.stringToSecretKey(SecretKeyString)

        val encoder = JwtTokenEncoder.fromSecretKey(secretKey)
        val decoder = JwtTokenDecoder.fromSecretKey(secretKey)

        val uuid = UUID.randomUUID()

        val token = encoder.createJwt("test", uuid)

        val userVo = decoder.getUserVo(token)

        assertEquals("test", userVo.nickName)
        assertEquals(uuid, userVo.userId)

        val secretKeyRe = ketConverter.secretKeyToString(secretKey)

        assertEquals(SecretKeyString, secretKeyRe)
    }

    @Test
    @DisplayName("Jwk 변환 테스트")
    fun jwkTest() {
        val publicKey = ketConverter.stringToPublicKey(publicKeyString)

        val jwk = ketConverter.publicKeyToJwk(publicKey)

        assertNotNull(jwk)

        val rePublicKey = ketConverter.jwkToPublicKey(jwk)

        assertNotNull(rePublicKey)

        assertEquals(publicKey, rePublicKey)
    }
}