package org.lelestacia.qurban_ticketing.domain.repository

import org.lelestacia.qurban_ticketing.util.Hour
import org.lelestacia.qurban_ticketing.util.Location
import org.lelestacia.qurban_ticketing.util.Minute
import org.lelestacia.qurban_ticketing.util.PickupDate

interface UtilRepository {
    suspend fun importUsersFromExcel(uri: String): Int
    suspend fun printCoupons(
        qurbanLocation: Location,
        qurbanPickupDate: PickupDate,
        qurbanStartTIme: Pair<Hour, Minute>,
        qurbanFinishTime: Pair<Hour, Minute>
    )
}