package com.yareach.socketjamcommon.config.swagger

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme

@SecurityScheme(
    name = "JWTAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
class SwaggerSecurityConfig {}