package org.lelestacia.qurban_ticketing.di


import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.lelestacia.qurban_ticketing.data.db.QurbanDB
import org.lelestacia.qurban_ticketing.ui.user.add_edit.UserAddEditViewmodel
import org.lelestacia.qurban_ticketing.ui.user_management.UserManagementViewModel
import org.lelestacia.qurban_ticketing.util.Navigator
import org.lelestacia.qurban_ticketing.util.route.Dashboard

val androidModule = module {
    single {
        Navigator(Dashboard)
    }

    single<RoomDatabase.Builder<QurbanDB>> {
        val appContext = androidContext().applicationContext
        val dbFile = appContext.getDatabasePath("my_room.db")
        Room.databaseBuilder<QurbanDB>(
            context = appContext,
            name = dbFile.absolutePath
        )
    }

    viewModelOf(::UserManagementViewModel)
    viewModel {
        UserAddEditViewmodel(
            get(),
            getOrNull(),
            get(),
            get()
        )
    }
}