package org.lelestacia.qurban_ticketing.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.lelestacia.qurban_ticketing.domain.model.Member

interface MemberRepository {

    suspend fun insertMember(member: Member)

    fun getMember(): Flow<PagingData<Member>>
}