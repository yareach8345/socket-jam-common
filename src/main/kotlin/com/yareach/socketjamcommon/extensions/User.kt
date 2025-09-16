package com.yareach.socketjamcommon.extensions

import com.yareach.socketjamcommon.config.security.CustomUserDetail
import com.yareach.socketjamcommon.dto.user.UserDto
import java.util.UUID

fun CustomUserDetail.toUserDto() = UserDto(
    UUID.fromString(userId),
    nickName
)