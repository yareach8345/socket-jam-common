package com.yareach.socketjamcommon.extensions

import com.yareach.socketjamcommon.dto.JwksDto
import com.yareach.socketjamcommon.vo.auth.JwkVo

fun JwkVo.toJwksDto() = JwksDto(
    kty = kty,
    e = e,
    n = n,
    alg = "RS256",
    use = "sig",
    kid = "main-key",
)

fun JwksDto.toJwk() = JwkVo(
    kty = kty,
    e = e,
    n = n,
)