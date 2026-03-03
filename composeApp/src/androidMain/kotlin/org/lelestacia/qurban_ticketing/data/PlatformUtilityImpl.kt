package org.lelestacia.qurban_ticketing.data

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import dev.zwander.kotlin.file.FileUtils
import kotlinx.io.IOException
import kotlinx.io.asOutputStream
import org.lelestacia.qurban_ticketing.data.utility.PlatformUtility
import org.lelestacia.qurban_ticketing.util.getDocumentDirectory
import java.io.OutputStream

class PlatformUtilityImpl(
    private val context: Context
) : PlatformUtility {

    override fun createQurbanTicketAndGetOS(documentName: String): OutputStream? {
        return if (Build.VERSION.SDK_INT >= 29) {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, documentName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Documents/")
            }

            val uri = context.contentResolver.insert(MediaStore.Files.getContentUri("external"), values)
            context.contentResolver.openOutputStream(
                uri ?: throw IOException("Failed to create Coupon"), "rw"
            )
        } else {
            val file = FileUtils.fromString(
                input = "${getDocumentDirectory()}/$documentName.pdf", isDirectory = false
            )

            file?.openOutputStream()?.asOutputStream()
        }
    }
}