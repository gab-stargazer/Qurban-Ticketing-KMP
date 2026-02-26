package org.lelestacia.qurban_ticketing.data.repository

import org.lelestacia.qurban_ticketing.data.dao.UserDao
import org.lelestacia.qurban_ticketing.data.utility.ExcelUtility
import org.lelestacia.qurban_ticketing.domain.repository.UtilRepository

class UtilRepositoryImpl(
    private val userDao: UserDao,
    private val excelUtility: ExcelUtility
) : UtilRepository {
    override suspend fun importUsersFromExcel(uri: String): Int {
        val users = excelUtility.importMemberFromExcel(uri)
        userDao.inserts(users)
        return users.size
    }
}