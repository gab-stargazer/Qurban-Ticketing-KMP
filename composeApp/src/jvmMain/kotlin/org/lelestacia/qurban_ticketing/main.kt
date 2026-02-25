package org.lelestacia.qurban_ticketing

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
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
import org.lelestacia.qurban_ticketing.util.route.UserList

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
        Row {
            NavigationRail {
                AnimatedContent(navigator.backstack.isRoot()) { isRootScreen ->
                    when (isRootScreen) {
                        true -> {
                            Column {
                                NavigationRailItem(
                                    selected = navigator.backstack.last() is UserList,
                                    onClick = {
                                        navigator.navigateTo(UserList)
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.List,
                                            contentDescription = "Member List Screen"
                                        )
                                    }
                                )
                            }
                        }

                        false -> {
                            NavigationRailItem(
                                selected = false,
                                onClick = {
                                    navigator.goBack()
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Member List Screen"
                                    )
                                }
                            )
                        }
                    }
                }
            }

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
}