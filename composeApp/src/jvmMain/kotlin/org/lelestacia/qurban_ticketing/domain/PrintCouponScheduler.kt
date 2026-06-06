package org.lelestacia.qurban_ticketing.domain

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.window.Notification
import androidx.compose.ui.window.TrayState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.lelestacia.qurban_ticketing.domain.background_scheduler.BackgroundScheduler
import org.lelestacia.qurban_ticketing.domain.repository.UtilRepository
import org.lelestacia.qurban_ticketing.util.Hour
import org.lelestacia.qurban_ticketing.util.Location
import org.lelestacia.qurban_ticketing.util.Minute
import org.lelestacia.qurban_ticketing.util.PickupDate

class PrintCouponScheduler(
    private val repository: UtilRepository,
    private val snackbarHostState: SnackbarHostState,
    private val trayState: TrayState
): BackgroundScheduler {

    override fun execute(vararg input: Any) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.printCoupons(
                qurbanLocation = Location(input[0] as String),
                qurbanPickupDate = PickupDate(input[1] as String),
                qurbanStartTIme = Pair(Hour(input[2] as Int), Minute(input[3] as Int)),
                qurbanFinishTime = Pair(Hour(input[4] as Int), Minute(input[5] as Int))
            )

            withContext(Dispatchers.Main) {
                trayState.sendNotification(Notification(title = "Kupon Selesai Dicetak", message = "Kupon selesai dicetak dan disimpan pada folder dokumen"))
            }
        }
    }
}