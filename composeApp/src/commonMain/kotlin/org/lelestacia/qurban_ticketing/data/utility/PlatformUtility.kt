package org.lelestacia.qurban_ticketing.data.utility

import java.io.OutputStream

interface PlatformUtility {

    fun createQurbanTicketAndGetOS(documentName: String): OutputStream?
}