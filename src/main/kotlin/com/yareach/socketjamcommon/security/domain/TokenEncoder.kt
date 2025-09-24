package com.yareach.socketjamcommon.security.domain

import com.yareach.socketjamcommon.user.model.UserIdentify
import java.util.UUID

interface TokenEncoder {
    fun createJwt(userId: UUID): String

    fun createJwt(userIdentify: UserIdentify): String = createJwt(userIdentify.userId)
}