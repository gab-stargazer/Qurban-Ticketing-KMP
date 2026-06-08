package org.lelestacia.qurban_ticketing.util

expect class FileStorage {

    fun saveCoupon(
        fileName: String,
        data: ByteArray
    ): String
}