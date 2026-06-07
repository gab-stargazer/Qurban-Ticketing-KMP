package org.lelestacia.qurban_ticketing.domain

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import co.touchlab.kermit.Logger
import org.lelestacia.qurban_ticketing.data.ExportDataWorker
import org.lelestacia.qurban_ticketing.domain.background_scheduler.BackgroundScheduler

class ExportDataScheduler(
    private val context: Context
) : BackgroundScheduler {

    override fun execute(vararg input: Any) {
        Logger.d("Export Data Scheduler Called")

        val workRequest = OneTimeWorkRequestBuilder<ExportDataWorker>()
            .build()

        WorkManager.getInstance(context)
            .enqueue(workRequest)
    }
}