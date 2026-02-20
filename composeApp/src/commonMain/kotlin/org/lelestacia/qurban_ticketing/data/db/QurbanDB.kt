package org.lelestacia.qurban_ticketing.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import org.lelestacia.qurban_ticketing.data.dao.MemberDao
import org.lelestacia.qurban_ticketing.data.entity.MemberEntity

@Database(
    entities = [
        MemberEntity::class
    ],
    version = 1,
    exportSchema = true
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class QurbanDB : RoomDatabase() {
    abstract fun memberDao(): MemberDao
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<QurbanDB> {
    override fun initialize(): QurbanDB
}