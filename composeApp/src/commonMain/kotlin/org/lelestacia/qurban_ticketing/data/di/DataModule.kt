package org.lelestacia.qurban_ticketing.data.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.binds
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.lelestacia.qurban_ticketing.data.dao.MemberDao
import org.lelestacia.qurban_ticketing.data.db.QurbanDB
import org.lelestacia.qurban_ticketing.data.repository.MemberRepositoryImpl
import org.lelestacia.qurban_ticketing.domain.repository.MemberRepository

val dataModule = module {
    single<QurbanDB> {
        get<RoomDatabase.Builder<QurbanDB>>()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    single<MemberDao> {
        get<QurbanDB>().memberDao()
    }

    singleOf(::MemberRepositoryImpl) {
        binds(listOf(MemberRepository::class))
    }
}