package org.lelestacia.qurban_ticketing.domain.viewmodel.member.add

import arrow.optics.optics
import org.lelestacia.qurban_ticketing.domain.model.Status
import org.lelestacia.qurban_ticketing.domain.model.Type

@optics
data class MemberAddState(
    val name: String = "",
    val phoneNumber: String = "",
    val status: Status = Status.Recipient,
    val type: Type? = null
) {
    companion object
}