package org.lelestacia.qurban_ticketing.domain.viewmodel.member.list

import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource

@Immutable
data class DialogPrintCouponState(
    val location: String = "",
    val locationError: StringResource? = null,

    val datePickerState: DatePickerState = DatePickerState(
        locale = CalendarLocale.forLanguageTag("id"),
        initialDisplayMode = DisplayMode.Picker,
    ),
    val datePickerStateError: StringResource? = null
) {
    companion object
}

sealed interface DialogPrintCouponEvent : UserManagementEvent {
    data class OnLocationChanged(val newLocation: String) : DialogPrintCouponEvent
    data class OnDatePicked(val selectedDate: Long) : DialogPrintCouponEvent
    data object OnSelectedPickupDateChanged : DialogPrintCouponEvent
    data object OnPrintCouponConfirmedWithPermission : DialogPrintCouponEvent
    data object OnPrintCouponConfirmedWithoutPermission : DialogPrintCouponEvent
}
