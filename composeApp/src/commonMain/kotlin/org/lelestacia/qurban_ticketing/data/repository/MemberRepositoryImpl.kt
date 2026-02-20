package org.lelestacia.qurban_ticketing.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.lelestacia.qurban_ticketing.data.dao.MemberDao
import org.lelestacia.qurban_ticketing.data.entity.MemberEntity
import org.lelestacia.qurban_ticketing.data.entity.toDomain
import org.lelestacia.qurban_ticketing.data.entity.toEntity
import org.lelestacia.qurban_ticketing.domain.model.Member
import org.lelestacia.qurban_ticketing.domain.repository.MemberRepository

class MemberRepositoryImpl(
    private val memberDao: MemberDao
) : MemberRepository {

    override suspend fun insertMember(member: Member) {
        memberDao.insert(member.toEntity())
    }

    override fun getMember(): Flow<PagingData<Member>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                initialLoadSize = 40
            ),
            pagingSourceFactory = {
                memberDao.getAllMember()
            }
        ).flow.map { it.map(MemberEntity::toDomain) }
    }
}