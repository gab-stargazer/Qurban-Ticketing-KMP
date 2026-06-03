package org.lelestacia.qurban_ticketing.di


import androidx.datastore.core.FileStorage
import androidx.datastore.preferences.core.PreferencesFileSerializer
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.binds
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.lelestacia.qurban_ticketing.Constant.IMPORT_DATA_SCHEDULER
import org.lelestacia.qurban_ticketing.Constant.PRINT_COUPON_SCHEDULER
import org.lelestacia.qurban_ticketing.data.ImportDataWorker
import org.lelestacia.qurban_ticketing.data.PlatformUtilityImpl
import org.lelestacia.qurban_ticketing.data.PrintCouponWorker
import org.lelestacia.qurban_ticketing.data.db.AppSettings
import org.lelestacia.qurban_ticketing.data.db.QurbanDB
import org.lelestacia.qurban_ticketing.data.db.createDataStore
import org.lelestacia.qurban_ticketing.data.db.dataStoreFileName
import org.lelestacia.qurban_ticketing.data.repository.SettingRepositoryImpl
import org.lelestacia.qurban_ticketing.data.utility.PlatformUtility
import org.lelestacia.qurban_ticketing.domain.ImportDataScheduler
import org.lelestacia.qurban_ticketing.domain.PrintCouponScheduler
import org.lelestacia.qurban_ticketing.domain.background_scheduler.BackgroundScheduler
import org.lelestacia.qurban_ticketing.domain.repository.SettingRepository
import org.lelestacia.qurban_ticketing.ui.user.add_edit.UserAddEditViewmodel
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

    singleOf(::PlatformUtilityImpl) {
        binds(listOf(PlatformUtility::class))
    }

    factoryOf(::ImportDataScheduler) {
        named(IMPORT_DATA_SCHEDULER)
        binds(listOf(BackgroundScheduler::class))
    }

    factoryOf(::PrintCouponScheduler) {
        named(PRINT_COUPON_SCHEDULER)
        binds(listOf(BackgroundScheduler::class))
    }

    single {
        createDataStore(
            storage = FileStorage(
                serializer = PreferencesFileSerializer,
                produceFile = { androidContext().applicationContext.filesDir.resolve(dataStoreFileName) }
            )
        )
    }

    singleOf(::AppSettings)

    singleOf(::SettingRepositoryImpl) {
        binds(listOf(SettingRepository::class))
    }

    workerOf(::ImportDataWorker)
    workerOf(::PrintCouponWorker)


    viewModel {
        UserAddEditViewmodel(
            get(),
            getOrNull(),
            get(),
            get()
        )
    }
}