package org.lelestacia.qurban_ticketing.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import org.lelestacia.qurban_ticketing.domain.model.Member
import org.lelestacia.qurban_ticketing.domain.repository.MemberRepository

class MemberListViewModel(
    private val repository: MemberRepository
) : ViewModel() {

    fun getMember(): Flow<PagingData<Member>> = repository
        .getMember()
        .cachedIn(viewModelScope)
}