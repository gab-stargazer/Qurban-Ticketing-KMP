package org.lelestacia.qurban_ticketing.util

import android.os.Environment

actual fun getDocumentDirectory(): String {
    return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()
}