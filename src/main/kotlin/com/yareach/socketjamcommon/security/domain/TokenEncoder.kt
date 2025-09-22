package com.yareach.socketjamcommon.security.domain

import com.yareach.socketjamcommon.user.dto.UserAuthDto
import java.util.UUID

interface TokenEncoder {
    fun createJwt(userId: UUID): String

    fun createJwt(userAuthDeo: UserAuthDto): String = createJwt(userAuthDeo.userId)
}