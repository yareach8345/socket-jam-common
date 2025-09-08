package com.yareach.socketjamcommon.dto

data class JwksDto(
    val kty: String,
    val e: String,
    val n: String,
    val alg: String,
    val use: String,
    val kid: String
)