@file:OptIn(ExperimentalUuidApi::class)

package org.lelestacia.qurban_ticketing.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.lelestacia.qurban_ticketing.domain.model.Member
import org.lelestacia.qurban_ticketing.domain.model.Status
import org.lelestacia.qurban_ticketing.domain.model.Type
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "member")
data class MemberEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "phone_number")
    val phone: String?,

    @ColumnInfo(name = "address")
    val address: String?,

    @ColumnInfo(name = "status")
    val status: Status,

    @ColumnInfo(name = "type")
    val type: Type?
)


fun MemberEntity.toDomain(): Member =
    Member(
        id = Uuid.parse(id),
        name = name,
        phoneNumber = phone,
        address = address,
        status = status,
        type = type,
    )

fun Member.toEntity(): MemberEntity =
    MemberEntity(
        id = id.toString(),
        name = name,
        phone = phoneNumber,
        address = address,
        status = status,
        type = type,
    )