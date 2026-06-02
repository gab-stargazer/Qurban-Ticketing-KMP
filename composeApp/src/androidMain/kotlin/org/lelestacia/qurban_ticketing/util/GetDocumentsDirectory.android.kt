package org.lelestacia.qurban_ticketing.util

import android.os.Build
import android.os.Environment

actual fun getDocumentDirectory(): String {
    return when {
        Build.VERSION.SDK_INT >= 30 ->
            Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                .toString()

        else -> {
            val documentsDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

            if (!documentsDir.exists()) {
                documentsDir.mkdirs()
            }

            documentsDir.toString()
        }
    }
}