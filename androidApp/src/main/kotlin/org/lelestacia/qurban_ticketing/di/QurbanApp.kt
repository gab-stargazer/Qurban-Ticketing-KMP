package org.lelestacia.qurban_ticketing.di

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.lelestacia.qurban_ticketing.data.di.dataModule
import org.lelestacia.qurban_ticketing.domain.di.domainModule
import org.lelestacia.qurban_ticketing.domain.repository.SettingRepository
import org.lelestacia.qurban_ticketing.route.routeModule
import java.util.Locale

class QurbanApp : Application() {

    val settingsRepository by inject<SettingRepository>()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(dataModule, domainModule, androidModule, routeModule)
            androidContext(this@QurbanApp)
            workManagerFactory()
        }

        CoroutineScope(Dispatchers.IO).launch {
            settingsRepository.readPreferredLanguage().collectLatest {
                Locale.setDefault(Locale.forLanguageTag(it))
            }
        }
    }
}