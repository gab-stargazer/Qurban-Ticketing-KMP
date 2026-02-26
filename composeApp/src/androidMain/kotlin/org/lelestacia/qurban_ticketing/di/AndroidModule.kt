package org.lelestacia.qurban_ticketing.di


import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.binds
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.lelestacia.qurban_ticketing.data.ImportDataWorker
import org.lelestacia.qurban_ticketing.data.PrintCouponWorker
import org.lelestacia.qurban_ticketing.data.db.QurbanDB
import org.lelestacia.qurban_ticketing.domain.ImportDataScheduler
import org.lelestacia.qurban_ticketing.domain.PrintCouponScheduler
import org.lelestacia.qurban_ticketing.domain.background_scheduler.BackgroundScheduler
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

    factoryOf(::ImportDataScheduler) {
        named("Import Data Scheduler")
        binds(listOf(BackgroundScheduler::class))
    }

    factoryOf(::PrintCouponScheduler) {
        named("Print Coupon Scheduler")
        binds(listOf(BackgroundScheduler::class))
    }

    workerOf(::ImportDataWorker)
    workerOf(::PrintCouponWorker)

    viewModel {
        UserManagementViewModel(
            get(),
            get(qualifier = named(name = "Import Data Scheduler")),
            get(qualifier = named(name = "Print Coupon Scheduler")),
        )
    }
    viewModel {
        UserAddEditViewmodel(
            get(),
            getOrNull(),
            get(),
            get()
        )
    }
}