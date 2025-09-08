package com.yareach.socketjamcommon.utils

import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import kotlin.io.bufferedReader
import kotlin.io.readText
import kotlin.io.use

@Component
class ResourceUtil {
    fun readResourceFile(path: String): String {
        val resource = ClassPathResource(path)
        return resource.inputStream.bufferedReader().use { it.readText() }
    }
}