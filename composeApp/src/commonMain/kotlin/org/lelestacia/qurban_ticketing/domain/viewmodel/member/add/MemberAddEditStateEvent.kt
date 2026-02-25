package org.lelestacia.qurban_ticketing.domain.viewmodel.member.add

import arrow.optics.optics
import org.jetbrains.compose.resources.StringResource
import org.lelestacia.qurban_ticketing.domain.model.Status
import org.lelestacia.qurban_ticketing.domain.model.Status.Recipient
import org.lelestacia.qurban_ticketing.domain.model.Type
import org.lelestacia.qurban_ticketing.domain.model.Type.Cow
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit.ScreenType

@optics
data class MemberAddEditState(
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

sealed class MemberAddEditEvent {
    data class OnNameChanged(val name: String) : MemberAddEditEvent()
    data class OnAddressChanged(val address: String) : MemberAddEditEvent()
    data class OnStatusChanged(val newQurbanStatus: Status) : MemberAddEditEvent()
    data class OnTypeChanged(val newQurbanType: Type) : MemberAddEditEvent()
    data object OnBackPressed : MemberAddEditEvent()
    data object OnAddEditPressed : MemberAddEditEvent()
    data object OnDeletePressed : MemberAddEditEvent()
}