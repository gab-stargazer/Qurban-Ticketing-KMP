package org.lelestacia.qurban_ticketing.route

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parameterSetOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import org.lelestacia.qurban_ticketing.domain.viewmodel.member.list.UserManagementViewModel
import org.lelestacia.qurban_ticketing.ui.MainMenu
import org.lelestacia.qurban_ticketing.ui.user.add_edit.UserAddEditViewmodel
import org.lelestacia.qurban_ticketing.ui.user_add_edit.UserAddEditScreen
import org.lelestacia.qurban_ticketing.ui.user_management.UserManagementScreen
import org.lelestacia.qurban_ticketing.util.Navigator
import org.lelestacia.qurban_ticketing.util.route.Dashboard
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit
import org.lelestacia.qurban_ticketing.util.route.UserList

@OptIn(KoinExperimentalAPI::class)
val routeModule = module {
    navigation<Dashboard> {
        val navigator = koinInject<Navigator>()
        MainMenu(
            navigateTo = { destination ->
                navigator.navigateTo(destination)
            }
        )
    }

    navigation<UserList> {
        val viewModel = koinViewModel<UserManagementViewModel>()
        val navigator = koinInject<Navigator>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        UserManagementScreen(
            state = state,
            onEvent = viewModel::onEvent,
            onNavigateTo = navigator::navigateTo,
            onBackPressed = navigator::goBack
        )
    }

    navigation<UserAddEdit> {
        val viewmodel = koinViewModel<UserAddEditViewmodel>(
            parameters = {
                parameterSetOf(
                    it.screenType,
                    it.initialData
                )
            }
        )
        val navigator = koinInject<Navigator>()
        val state by viewmodel.state.collectAsStateWithLifecycle()
        UserAddEditScreen(
            state = state,
            onEvent = viewmodel::onEvent,
            onBackPressed = navigator::goBack
        )
    }
}