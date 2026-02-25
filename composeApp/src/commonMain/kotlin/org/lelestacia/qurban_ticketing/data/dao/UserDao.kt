package org.lelestacia.qurban_ticketing.data.dao

import androidx.paging.PagingSource
import androidx.room.*
import org.lelestacia.qurban_ticketing.data.entity.UserEntity
import org.lelestacia.qurban_ticketing.domain.model.Status

@Dao
interface UserDao {

    @Insert(
        onConflict = OnConflictStrategy.ABORT,
        entity = UserEntity::class
    )
    suspend fun insert(user: UserEntity)

    @Insert(
        onConflict = OnConflictStrategy.ABORT,
        entity = UserEntity::class
    )
    suspend fun inserts(user: List<UserEntity>)

    @Update(
        onConflict = OnConflictStrategy.ABORT,
        entity = UserEntity::class,
    )
    suspend fun update(user: UserEntity)

    @Query(
        value = """
            SELECT * FROM user
            WHERE name like '%' || :name || '%' 
            ORDER BY name COLLATE NOCASE ASC
        """
    )
    fun getAllUsers(name: String): PagingSource<Int, UserEntity>

    @Query(
        value = """
            SELECT * FROM user
            WHERE name like '%' || :name || '%' AND status == :status
            ORDER BY name ASC
        """
    )
    fun getUsersByStatus(name: String, status: Status): PagingSource<Int, UserEntity>

    @Delete(entity = UserEntity::class)
    suspend fun deleteMember(user: UserEntity)
}