package org.lelestacia.qurban_ticketing.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.lelestacia.qurban_ticketing.data.di.dataModule
import org.lelestacia.qurban_ticketing.domain.di.domainModule
import org.lelestacia.qurban_ticketing.route.routeModule

class QurbanApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(dataModule, domainModule, androidModule, routeModule)
            androidContext(this@QurbanApp)
        }
    }
}