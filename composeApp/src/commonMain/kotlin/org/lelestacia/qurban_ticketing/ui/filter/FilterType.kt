package org.lelestacia.qurban_ticketing.ui.filter

import org.jetbrains.compose.resources.StringResource
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.filter_type_all
import qurbanticketing.composeapp.generated.resources.filter_type_participant
import qurbanticketing.composeapp.generated.resources.filter_type_recipient

enum class FilterType(val uiText: StringResource) {
    All(Res.string.filter_type_all),
    Participant(Res.string.filter_type_participant),
    Recipient(Res.string.filter_type_recipient)
}