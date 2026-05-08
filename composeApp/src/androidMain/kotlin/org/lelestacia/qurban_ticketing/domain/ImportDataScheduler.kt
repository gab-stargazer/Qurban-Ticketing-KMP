package org.lelestacia.qurban_ticketing.domain

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import co.touchlab.kermit.Logger
import org.lelestacia.qurban_ticketing.data.ImportDataWorker
import org.lelestacia.qurban_ticketing.domain.background_scheduler.BackgroundScheduler

class ImportDataScheduler(
    private val context: Context
) : BackgroundScheduler {

    override fun execute(vararg input: Any) {
        Logger.d("ImportDataScheduler Called with URI: $input")

        val workRequest = OneTimeWorkRequestBuilder<ImportDataWorker>()
            .setInputData(
                Data.Builder()
                    .putString(ImportDataWorker.INPUT_DATA_URL, input[0] as String)
                    .build()
            )
            .build()

        WorkManager.getInstance(context)
            .enqueue(workRequest)
    }
}