@file:OptIn(ExperimentalUuidApi::class)

package org.lelestacia.qurban_ticketing.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.lelestacia.qurban_ticketing.domain.model.Status
import org.lelestacia.qurban_ticketing.domain.model.Type
import org.lelestacia.qurban_ticketing.domain.model.User
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "user")
data class UserEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "address")
    val address: String?,

    @ColumnInfo(name = "status")
    val status: Status,

    @ColumnInfo(name = "type")
    val type: Type?
)


fun UserEntity.toDomain(): User =
    User(
        id = Uuid.parse(id),
        name = name,
        address = address,
        status = status,
        type = type,
    )

fun User.toEntity(): UserEntity =
    UserEntity(
        id = id.toString(),
        name = name,
        address = address,
        status = status,
        type = type,
    )