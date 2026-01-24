package com.evandhardspace.chatapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform