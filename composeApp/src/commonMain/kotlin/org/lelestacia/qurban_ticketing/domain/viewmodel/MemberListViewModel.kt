package org.lelestacia.qurban_ticketing.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import org.lelestacia.qurban_ticketing.domain.model.User
import org.lelestacia.qurban_ticketing.domain.repository.UserRepository

class MemberListViewModel(
    private val repository: UserRepository
) : ViewModel() {

    fun getMember(): Flow<PagingData<User>> = repository
        .getUsers("")
        .cachedIn(viewModelScope)
}