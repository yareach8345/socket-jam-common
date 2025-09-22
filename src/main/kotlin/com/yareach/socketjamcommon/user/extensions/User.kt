package com.yareach.socketjamcommon.user.extensions

import com.yareach.socketjamcommon.config.security.CustomUserDetail
import com.yareach.socketjamcommon.user.dto.UserAuthDto

fun CustomUserDetail.toUserAuthDto() = UserAuthDto( userId = userId )