package org.lelestacia.qurban_ticketing.domain

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.lelestacia.qurban_ticketing.domain.background_scheduler.BackgroundScheduler
import org.lelestacia.qurban_ticketing.domain.repository.UtilRepository
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.notification_body_save_coupon

class PrintCouponScheduler(
    private val repository: UtilRepository,
    private val snackbarHostState: SnackbarHostState
): BackgroundScheduler {

    override fun execute(vararg input: Any) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.saveCoupons(
                qurbanLocation = input.first() as String,
                qurbanPickupDate = input.last() as String
            )
            snackbarHostState.showSnackbar(getString(Res.string.notification_body_save_coupon))
        }
    }
}