package org.lelestacia.qurban_ticketing.data

import dev.zwander.kotlin.file.FileUtils
import kotlinx.io.asOutputStream
import org.lelestacia.qurban_ticketing.data.utility.PlatformUtility
import org.lelestacia.qurban_ticketing.util.getDocumentDirectory
import java.io.OutputStream

class PlatformUtilityImpl : PlatformUtility {

    override fun createQurbanTicketAndGetOS(documentName: String): OutputStream? {
        var platformFile = FileUtils.fromString(
            input = "${getDocumentDirectory()}/$documentName.pdf",
            isDirectory = false
        )

        if (platformFile?.getExists() ?: false) {
            platformFile.delete()
            platformFile = FileUtils.fromString(
                input = "${getDocumentDirectory()}/$documentName.pdf",
                isDirectory = false
            )
        }

        return platformFile?.openOutputStream()?.asOutputStream()
    }
}