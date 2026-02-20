package org.lelestacia.qurban_ticketing.domain.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.lelestacia.qurban_ticketing.domain.viewmodel.MemberListViewModel

val domainModule = module {
    viewModelOf(::MemberListViewModel)
}