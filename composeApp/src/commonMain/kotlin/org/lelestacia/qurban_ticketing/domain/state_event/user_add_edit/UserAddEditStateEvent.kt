package org.lelestacia.qurban_ticketing.domain.state_event.user_add_edit

import arrow.optics.optics
import org.jetbrains.compose.resources.StringResource
import org.lelestacia.qurban_ticketing.domain.model.Status
import org.lelestacia.qurban_ticketing.domain.model.Status.Recipient
import org.lelestacia.qurban_ticketing.domain.model.Type
import org.lelestacia.qurban_ticketing.domain.model.Type.Cow
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit.ScreenType

@optics
data class UserAddEditState(
    val screenType: ScreenType = ScreenType.ADD,

    //  Personal Information
    val name: String = "",
    val nameError: StringResource? = null,
    val address: String = "",

    //  Qurban status and type
    val status: Status = Recipient,
    val type: Type = Cow,

    val isLoading: Boolean = false,
) {
    companion object
}

sealed class UserAddEditEvent {
    data class OnNameChanged(val name: String) : UserAddEditEvent()
    data class OnAddressChanged(val address: String) : UserAddEditEvent()
    data class OnStatusChanged(val newQurbanStatus: Status) : UserAddEditEvent()
    data class OnTypeChanged(val newQurbanType: Type) : UserAddEditEvent()
    data object OnBackPressed : UserAddEditEvent()
    data object OnAddEditPressed : UserAddEditEvent()
    data object OnDeletePressed : UserAddEditEvent()
}