package org.lelestacia.qurban_ticketing.domain.repository

interface UtilRepository {
    suspend fun importUsersFromExcel(uri: String): Int
    suspend fun saveCoupons(
        qurbanLocation: String,
        qurbanPickupDate: String
    )
}