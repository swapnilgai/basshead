package com.org.basshead

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform