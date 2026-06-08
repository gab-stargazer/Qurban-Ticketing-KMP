package org.lelestacia.qurban_ticketing.domain.di

import androidx.compose.material3.SnackbarHostState
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.lelestacia.qurban_ticketing.Constant.EXPORT_DATA_SCHEDULER
import org.lelestacia.qurban_ticketing.Constant.IMPORT_DATA_SCHEDULER
import org.lelestacia.qurban_ticketing.Constant.PRINT_COUPON_SCHEDULER
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
            printCouponScheduler = get(qualifier = named(name = PRINT_COUPON_SCHEDULER)),
        )
    }

    viewModel {
        SettingViewModel(
            settingRepository = get(),
            importDataScheduler = get(qualifier = named(name = IMPORT_DATA_SCHEDULER)),
            exportDataScheduler = get(qualifier = named(name = EXPORT_DATA_SCHEDULER))
        )
    }
}