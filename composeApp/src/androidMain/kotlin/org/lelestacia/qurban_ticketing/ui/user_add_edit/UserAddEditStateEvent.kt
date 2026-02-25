package org.lelestacia.qurban_ticketing.ui.user_add_edit

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.optics.optics
import org.jetbrains.compose.resources.StringResource
import org.lelestacia.qurban_ticketing.domain.model.Status
import org.lelestacia.qurban_ticketing.domain.model.Type
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit.ScreenType
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.error_name_cannot_be_empty

@optics
data class UserAddEditState(
    val screenType: ScreenType,
    val name: String = "",
    val nameError: StringResource? = null,
    val address: String = "",

    val qurbanStatus: Status = Status.Recipient,
    val qurbanType: Type = Type.Cow,


    val isLoading: Boolean = false,
) {

    fun validateName(): Either<StringResource, Unit> = either {
        ensure(name.isNotBlank()) { Res.string.error_name_cannot_be_empty }
    }

    companion object
}

sealed class UserAddEditEvent {

    data class OnNameChanged(val name: String) : UserAddEditEvent()
    data class OnAddressChanged(val newAddress: String) : UserAddEditEvent()
    data class OnStatusChanged(val newStatus: Status) : UserAddEditEvent()
    data class OnTypeChanged(val newType: Type) : UserAddEditEvent()
    data object OnBackPressed : UserAddEditEvent()
    data object OnAddEditPressed : UserAddEditEvent()
    data object OnDeletePressed : UserAddEditEvent()
}