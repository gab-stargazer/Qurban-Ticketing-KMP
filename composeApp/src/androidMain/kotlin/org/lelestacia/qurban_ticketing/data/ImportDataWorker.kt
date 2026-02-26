package org.lelestacia.qurban_ticketing.data

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.java.KoinJavaComponent.inject
import org.lelestacia.qurban_ticketing.R
import org.lelestacia.qurban_ticketing.domain.repository.UtilRepository

class ImportDataWorker(
    context: Context,
    parameters: WorkerParameters
) : CoroutineWorker(context, parameters) {

    private val repository by inject<UtilRepository>(clazz = UtilRepository::class.java)

    override suspend fun doWork(): Result {
        val input: String = inputData.getString(INPUT_DATA_URL)
            ?: return Result.failure()
        val userImported = repository.importUsersFromExcel(uri = input)

        val channel = NotificationChannel(
            "default_channel",
            "General Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "App general notifications"
        }


        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        if (
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val builder = NotificationCompat.Builder(applicationContext, "default_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Process Finished")
                .setContentText("Import data finished with a total of $userImported persons")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(applicationContext)) {
                notify(1001, builder.build())
            }
        }

        return Result.success()
    }

    companion object {
        const val INPUT_DATA_URL = "input_url"
    }
}