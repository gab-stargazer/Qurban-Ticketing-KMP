package org.lelestacia.qurban_ticketing.util

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.toAndroidUri
import io.github.vinceglb.filekit.readBytes

@OptIn(ExperimentalPermissionsApi::class)
fun PermissionStatus.isNotGranted(): Boolean {
    return !this.isGranted
}

suspend fun Context.handleImagePick(file: PlatformFile?, onPicked: (uri: String, bytes: ByteArray) -> Unit) {
    file?.let { file ->
        val uri = file.toAndroidUri()
        if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            try {
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        onPicked(uri.toString(), file.readBytes())
    }
}