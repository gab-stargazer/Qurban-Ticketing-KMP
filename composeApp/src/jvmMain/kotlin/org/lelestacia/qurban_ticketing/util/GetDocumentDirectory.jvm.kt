package org.lelestacia.qurban_ticketing.util

actual fun getDocumentDirectory(): String {
    val userHome = System.getProperty("user.home")
    return "$userHome/Documents/"
}