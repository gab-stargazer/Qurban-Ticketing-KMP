package org.lelestacia.qurban_ticketing.domain.model

import org.jetbrains.compose.resources.StringResource
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.qurban_status_participant
import qurbanticketing.composeapp.generated.resources.qurban_status_recipient

enum class Status(
    val uiText: StringResource
) {
    Recipient(Res.string.qurban_status_recipient),
    Participant(Res.string.qurban_status_participant)
}

fun Status.isParticipant(): Boolean = this == Status.Participant