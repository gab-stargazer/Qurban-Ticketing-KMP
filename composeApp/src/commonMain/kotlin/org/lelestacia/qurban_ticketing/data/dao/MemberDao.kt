package org.lelestacia.qurban_ticketing.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.lelestacia.qurban_ticketing.data.entity.MemberEntity

@Dao
interface MemberDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(member: MemberEntity)

    @Query(
        value = """
            SELECT * FROM member
            ORDER BY name ASC
        """
    )
    fun getAllMember(): PagingSource<Int, MemberEntity>
}