package org.lelestacia.qurban_ticketing.data

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.jetbrains.compose.resources.getString
import org.koin.java.KoinJavaComponent.inject
import org.lelestacia.qurban_ticketing.R
import org.lelestacia.qurban_ticketing.domain.repository.UtilRepository
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.notification_body_save_coupon
import qurbanticketing.composeapp.generated.resources.notification_title_process_failed
import qurbanticketing.composeapp.generated.resources.notification_title_process_finished
import kotlin.random.Random

class PrintCouponWorker(
    context: Context,
    parameters: WorkerParameters
) : CoroutineWorker(context, parameters) {

    private val repository by inject<UtilRepository>(clazz = UtilRepository::class.java)

    override suspend fun doWork(): Result {
        try {
            repository.saveCoupons(
                qurbanLocation = inputData.getString(LOCATION) ?: return Result.failure(),
                qurbanPickupDate = inputData.getString(PICKUP_DATE) ?: return Result.failure()
            )

            val channel = NotificationChannel(
                "default_channel",
                "General Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "App general notifications"
            }

            val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
            postNotification()

            return Result.success()
        } catch (e: Exception) {
            val notificationBuilder = NotificationCompat.Builder(applicationContext, "default_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(
                    getString(
                        resource = Res.string.notification_title_process_failed
                    )
                )
                .setContentText(e.message ?: e.stackTraceToString())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(applicationContext)) {
                if (Build.VERSION.SDK_INT >= 32) {
                    if (
                        ContextCompat.checkSelfPermission(
                            applicationContext,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        notify(Random.nextInt(), notificationBuilder.build())
                    }
                } else {
                    notify(Random.nextInt(), notificationBuilder.build())
                }
            }

            return Result.failure()
        }
    }

    private suspend fun postNotification() {
        val notificationBuilder = NotificationCompat.Builder(applicationContext, "default_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    resource = Res.string.notification_title_process_finished
                )
            )
            .setContentText(
                getString(
                    resource = Res.string.notification_body_save_coupon
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            if (Build.VERSION.SDK_INT >= 32) {
                if (
                    ContextCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    notify(Random.nextInt(), notificationBuilder.build())
                }
            } else {
                notify(Random.nextInt(), notificationBuilder.build())
            }
        }
    }

    companion object {
        const val LOCATION = "location"
        const val PICKUP_DATE = "pickupDate"
    }
}