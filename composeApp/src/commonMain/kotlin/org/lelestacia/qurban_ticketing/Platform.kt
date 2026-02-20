package org.lelestacia.qurban_ticketing

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform