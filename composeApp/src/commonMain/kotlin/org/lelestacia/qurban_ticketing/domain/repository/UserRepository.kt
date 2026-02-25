package org.lelestacia.qurban_ticketing.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.lelestacia.qurban_ticketing.domain.model.Status
import org.lelestacia.qurban_ticketing.domain.model.User

interface UserRepository {

    suspend fun insertUser(user: User)

    suspend fun updateUser(user: User)

    fun getUsers(name: String): Flow<PagingData<User>>

    fun getUsersByStatus(name: String, status: Status): Flow<PagingData<User>>

    suspend fun deleteMember(user: User)
}