package org.lelestacia.qurban_ticketing.data.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.binds
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.lelestacia.qurban_ticketing.data.dao.UserDao
import org.lelestacia.qurban_ticketing.data.db.QurbanDB
import org.lelestacia.qurban_ticketing.data.repository.UserRepositoryImpl
import org.lelestacia.qurban_ticketing.data.repository.UtilRepositoryImpl
import org.lelestacia.qurban_ticketing.data.utility.CouponUtility
import org.lelestacia.qurban_ticketing.data.utility.ExcelUtility
import org.lelestacia.qurban_ticketing.domain.repository.UserRepository
import org.lelestacia.qurban_ticketing.domain.repository.UtilRepository

val dataModule = module {
    single<QurbanDB> {
        get<RoomDatabase.Builder<QurbanDB>>()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    single<UserDao> {
        get<QurbanDB>().memberDao()
    }

    singleOf(::ExcelUtility)
    singleOf(::CouponUtility)

    singleOf(::UserRepositoryImpl) {
        binds(listOf(UserRepository::class))
    }

    singleOf(::UtilRepositoryImpl) {
        binds(listOf(UtilRepository::class))
    }
}