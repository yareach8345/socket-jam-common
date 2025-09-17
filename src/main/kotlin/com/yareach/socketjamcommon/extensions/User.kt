package com.yareach.socketjamcommon.extensions

import com.yareach.socketjamcommon.config.security.CustomUserDetail
import com.yareach.socketjamcommon.dto.user.UserDto

fun CustomUserDetail.toUserDto() = UserDto(
    userId = userId,
    nickName = nickName
)