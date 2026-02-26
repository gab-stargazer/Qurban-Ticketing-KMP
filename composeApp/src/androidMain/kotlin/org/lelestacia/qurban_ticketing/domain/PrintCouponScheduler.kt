package org.lelestacia.qurban_ticketing.domain

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import co.touchlab.kermit.Logger
import org.lelestacia.qurban_ticketing.data.PrintCouponWorker
import org.lelestacia.qurban_ticketing.domain.background_scheduler.BackgroundScheduler

class PrintCouponScheduler(
    private val context: Context
) : BackgroundScheduler {

    override fun execute(vararg input: Any) {
        Logger.d("PrintCouponScheduler Called")

        val workRequest = OneTimeWorkRequestBuilder<PrintCouponWorker>()
            .setInputData(
                Data.Builder()
                    .putString(PrintCouponWorker.LOCATION, input[0] as String)
                    .putString(PrintCouponWorker.PICKUP_DATE, input[1] as String)
                    .build()
            )
            .build()

        WorkManager.getInstance(context)
            .enqueue(workRequest)
    }
}