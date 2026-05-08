package org.lelestacia.qurban_ticketing.util

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.FileKitUserDirectory
import io.github.vinceglb.filekit.path
import io.github.vinceglb.filekit.userDirectory

actual fun getDocumentDirectory(): String {
    return FileKit.userDirectory(FileKitUserDirectory.Documents).path
}