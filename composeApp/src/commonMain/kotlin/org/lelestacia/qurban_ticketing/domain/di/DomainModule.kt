package org.lelestacia.qurban_ticketing.domain.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.lelestacia.qurban_ticketing.domain.viewmodel.MemberListViewModel
import org.lelestacia.qurban_ticketing.domain.viewmodel.member.add.MemberAddEditViewModel
import org.lelestacia.qurban_ticketing.ui.user.add_edit.UserAddEditViewmodel

val domainModule = module {
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
}