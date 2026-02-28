package org.lelestacia.qurban_ticketing.domain.di

import androidx.compose.material3.SnackbarHostState
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.lelestacia.qurban_ticketing.domain.viewmodel.MemberListViewModel
import org.lelestacia.qurban_ticketing.domain.viewmodel.member.add.MemberAddEditViewModel
import org.lelestacia.qurban_ticketing.domain.viewmodel.member.list.UserManagementViewModel
import org.lelestacia.qurban_ticketing.ui.user.add_edit.UserAddEditViewmodel

val domainModule = module {
    single { SnackbarHostState() }

    viewModelOf(::MemberListViewModel)
    viewModel {
        MemberAddEditViewModel(
            screenType = get(),
            initialData = getOrNull(),
            navigator = get(),
            repository = get()
        )
    }

    viewModelOf(::UserAddEditViewmodel)
    viewModel {
        UserManagementViewModel(
            userRepository = get(),
            importDataScheduler = get(qualifier = named(name = "Import Data Scheduler")),
            printCouponScheduler = get(qualifier = named(name = "Print Coupon Scheduler")),
        )
    }
}