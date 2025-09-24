package com.yareach.socketjamcommon.user.extensions

import com.yareach.socketjamcommon.config.security.CustomUserDetail
import com.yareach.socketjamcommon.user.model.UserIdentify

fun CustomUserDetail.toUserIdentify() = UserIdentify( userId = userId )