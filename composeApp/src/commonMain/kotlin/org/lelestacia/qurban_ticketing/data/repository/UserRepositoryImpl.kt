package org.lelestacia.qurban_ticketing.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.lelestacia.qurban_ticketing.data.dao.UserDao
import org.lelestacia.qurban_ticketing.data.entity.UserEntity
import org.lelestacia.qurban_ticketing.data.entity.toDomain
import org.lelestacia.qurban_ticketing.data.entity.toEntity
import org.lelestacia.qurban_ticketing.domain.model.Status
import org.lelestacia.qurban_ticketing.domain.model.User
import org.lelestacia.qurban_ticketing.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun insertUser(user: User) {
        userDao.insert(user.toEntity())
    }

    override suspend fun updateUser(user: User) {
        userDao.update(user.toEntity())
    }

    override fun getUsers(name: String): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                initialLoadSize = 40
            ),
            pagingSourceFactory = {
                userDao.getAllUsers(name)
            }
        ).flow.map { it.map(UserEntity::toDomain) }
    }

    override fun getUsersByStatus(
        name: String,
        status: Status
    ): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                initialLoadSize = 40
            ),
            pagingSourceFactory = {
                userDao.getUsersByStatus(name, status)
            }
        ).flow.map { it.map(UserEntity::toDomain) }
    }

    override suspend fun deleteMember(user: User) {
        userDao.deleteMember(user.toEntity())
    }
}