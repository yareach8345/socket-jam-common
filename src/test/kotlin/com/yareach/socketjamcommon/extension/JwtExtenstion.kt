package com.yareach.socketjamcommon.extension

import com.yareach.socketjamcommon.dto.auth.JwksDto
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