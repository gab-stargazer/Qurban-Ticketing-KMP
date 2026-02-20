package org.lelestacia.qurban_ticketing

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import org.koin.compose.koinInject
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.lelestacia.qurban_ticketing.data.di.dataModule
import org.lelestacia.qurban_ticketing.di.jvmModule
import org.lelestacia.qurban_ticketing.domain.di.domainModule
import org.lelestacia.qurban_ticketing.util.Navigator

@OptIn(KoinExperimentalAPI::class)
fun main() = application {

    startKoin {
        modules(dataModule, domainModule, jvmModule)
    }

    val navigator = koinInject<Navigator>()
    val entryProvider = koinEntryProvider<Any>()

    Window(
        onCloseRequest = ::exitApplication,
        title = "Qurban Ticketing",
    ) {
        NavDisplay(
            backStack = navigator.backstack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            onBack = { navigator.goBack() },
            entryProvider = entryProvider
        )
    }
}