package org.lelestacia.qurban_ticketing.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.lelestacia.qurban_ticketing.util.serializer.UUIDSerializer
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class Member(

    @Serializable(with = UUIDSerializer::class)
    val id: Uuid,

    @SerialName("name")
    val name: String,

    @SerialName("phone_number")
    val phoneNumber: String?,

    @SerialName("address")
    val address: String?,

    @SerialName("status")
    val status: Status,

    @SerialName("type")
    val type: Type?,
)
