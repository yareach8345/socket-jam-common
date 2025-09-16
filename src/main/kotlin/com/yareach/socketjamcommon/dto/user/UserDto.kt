package com.yareach.socketjamcommon.dto.user

import java.util.UUID

data class UserDto(
    val userId: UUID,
    val nickName: String,
)