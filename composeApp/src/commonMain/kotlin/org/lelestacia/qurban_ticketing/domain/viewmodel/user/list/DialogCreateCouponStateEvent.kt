package org.lelestacia.qurban_ticketing.domain.viewmodel.user.list

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource
import org.lelestacia.qurban_ticketing.util.Hour
import org.lelestacia.qurban_ticketing.util.Minute

@Immutable
@OptIn(ExperimentalMaterial3Api::class)
data class DialogPrintCouponState (
    val location: TextFieldState = TextFieldState(),
    val locationError: StringResource? = null,


    //  Date Picker
    val datePickerState: DatePickerState = DatePickerState(
        locale = CalendarLocale.forLanguageTag("id"),
        initialDisplayMode = DisplayMode.Picker,
    ),
    val datePickerStateError: StringResource? = null,

    //  Start Time
    val startTime: TimePickerState = TimePickerState(
        initialHour = 0,
        initialMinute = 0,
        is24Hour = true
    ),
    val startTimeError: StringResource? = null,

    //  Finish Time
    val finishTime: TimePickerState = TimePickerState(
        initialHour = 0,
        initialMinute = 0,
        is24Hour = true
    ),
    val finishTimeError: StringResource? = null,
) {
    companion object
}

sealed interface DialogPrintCouponEvent : UserListEvent {
    data class OnDatePicked(val selectedDate: Long) : DialogPrintCouponEvent
    data object OnSelectedPickupDateChanged : DialogPrintCouponEvent
    data class OnStartTimePicked(val hour: Hour, val minute: Minute) : DialogPrintCouponEvent
    data class OnFinishTimePicked(val hour: Hour, val minute: Minute) : DialogPrintCouponEvent
    data object OnPrintCouponConfirmed : DialogPrintCouponEvent
}
