package org.lelestacia.qurban_ticketing.util

import android.content.Context
import android.os.Environment
import java.io.File

actual class FileStorage(
    private val context: Context
) {
    actual fun saveCoupon(
        fileName: String,
        data: ByteArray
    ): String {
        val directory = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "coupons"
        ).apply {
            if (!exists()) mkdirs()
        }

        val file = File(directory, fileName)
        file.writeBytes(data)

        return file.absolutePath
    }
}