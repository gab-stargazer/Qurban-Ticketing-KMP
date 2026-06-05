package org.lelestacia.qurban_ticketing.data.repository

import org.lelestacia.qurban_ticketing.data.dao.UserDao
import org.lelestacia.qurban_ticketing.data.utility.CouponUtility
import org.lelestacia.qurban_ticketing.data.utility.ExcelUtility
import org.lelestacia.qurban_ticketing.domain.model.Type
import org.lelestacia.qurban_ticketing.domain.repository.UtilRepository
import org.lelestacia.qurban_ticketing.util.Hour
import org.lelestacia.qurban_ticketing.util.Location
import org.lelestacia.qurban_ticketing.util.Minute
import org.lelestacia.qurban_ticketing.util.PickupDate

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

    override suspend fun printCoupons(
        qurbanLocation: Location,
        qurbanPickupDate: PickupDate,
        qurbanStartTIme: Pair<Hour, Minute>,
        qurbanFinishTime: Pair<Hour, Minute>
    ) {
        couponUtility.saveCoupons(
            qurbanLocation = qurbanLocation,
            qurbanPickupDate = qurbanPickupDate,
            qurbanStartTime = qurbanStartTIme,
            qurbanFinishTime = qurbanFinishTime,
            userData = userDao.getAllUserData("").map {
                CouponUtility.CouponData(
                    name = it.name,
                    address = it.address.orEmpty(),
                    status = it.status,
                    type = it.type ?: Type.Cow
                )
            }
        )
    }
}