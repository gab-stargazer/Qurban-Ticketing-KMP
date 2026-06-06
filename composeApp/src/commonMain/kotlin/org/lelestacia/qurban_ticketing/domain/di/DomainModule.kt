package org.lelestacia.qurban_ticketing.domain.di

import androidx.compose.material3.SnackbarHostState
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.lelestacia.qurban_ticketing.domain.viewmodel.MemberListViewModel
import org.lelestacia.qurban_ticketing.domain.viewmodel.setting.SettingViewModel
import org.lelestacia.qurban_ticketing.domain.viewmodel.user_add_edit.UserAddEditViewModel
import org.lelestacia.qurban_ticketing.domain.viewmodel.user_list.UserListViewModel
import org.lelestacia.qurban_ticketing.ui.user.add_edit.UserAddEditViewmodel

val domainModule = module {
    single { SnackbarHostState() }

    viewModelOf(::MemberListViewModel)
    viewModel {
        UserAddEditViewModel(
            screenType = get(),
            initialData = getOrNull(),
            navigator = get(),
            repository = get()
        )
    }

    viewModelOf(::UserAddEditViewmodel)
    viewModel {
        UserListViewModel(
            userRepository = get(),
            importDataScheduler = get(qualifier = named(name = "Import Data Scheduler")),
            printCouponScheduler = get(qualifier = named(name = "Print Coupon Scheduler")),
        )
    }

    viewModelOf(::SettingViewModel)
}