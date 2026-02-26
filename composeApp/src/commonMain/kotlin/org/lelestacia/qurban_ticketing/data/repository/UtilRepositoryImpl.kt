package org.lelestacia.qurban_ticketing.data.repository

import org.lelestacia.qurban_ticketing.data.dao.UserDao
import org.lelestacia.qurban_ticketing.data.utility.CouponUtility
import org.lelestacia.qurban_ticketing.data.utility.ExcelUtility
import org.lelestacia.qurban_ticketing.domain.model.Type
import org.lelestacia.qurban_ticketing.domain.repository.UtilRepository

class UtilRepositoryImpl(
    private val userDao: UserDao,
    private val excelUtility: ExcelUtility,
    private val couponUtility: CouponUtility,
) : UtilRepository {
    override suspend fun importUsersFromExcel(uri: String): Int {
        val users = excelUtility.importMemberFromExcel(uri)
        userDao.inserts(users)
        return users.size
    }

    override suspend fun saveCoupons(
        qurbanLocation: String,
        qurbanPickupDate: String
    ) {
        couponUtility.saveCoupons(
            qurbanLocation = qurbanLocation,
            qurbanPickupDate = qurbanPickupDate,
            userData = userDao.getAllUserData("").map {
                CouponUtility.CouponData(
                    name = it.name,
                    status = it.status,
                    type = it.type ?: Type.Cow
                )
            }
        )
    }
}