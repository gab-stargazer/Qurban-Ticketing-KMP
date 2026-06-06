package org.lelestacia.qurban_ticketing.domain.viewmodel.user.list

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import arrow.optics.optics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.lelestacia.qurban_ticketing.domain.model.User
import org.lelestacia.qurban_ticketing.ui.dropdown.FilterType
import org.lelestacia.qurban_ticketing.ui.dropdown.FilterType.All


@Immutable
@optics
data class UserManagementState(
    val searchQuery: String = "",
    val filterType: FilterType = All,
    val isFilterMenuOpened: Boolean = false,
    val isFabMenuExpanded: Boolean = false,
    val isNotificationPermissionDialogOpened: Boolean = false,

    val isNotificationDialogForImportDataOpened: Boolean = false,
    val isNotificationDialogForPrintCouponOpened: Boolean = false,


    //  Permission Dialog




    //  Create Coupon
    val isPrintingReminderOpened: Boolean = false,
    val isPrintingDialogOpened: Boolean = false,
    val dialogPrintCouponState: DialogPrintCouponState = DialogPrintCouponState(),


    val users: Flow<PagingData<User>> = flowOf(),
    val selectedUserIndex: Int? = null,

    //  Trigger
    val shouldLaunchExcelLauncher: Boolean = false,
) {

    companion object
}

sealed interface UserManagementEvent {
    data class OnSearchQueryChanged(
        val newSearchQuery: String,
    ) : UserManagementEvent

    data class OnUserClicked(
        val index: Int?,
    ) : UserManagementEvent

    //  UI State Event
    data class OnFabMenuStateClicked(
        val newFabMenuState: Boolean
    ) : UserManagementEvent


    //  Print Coupon
    data class OnPrintCouponClicked(val isNotificationPermissionNeeded: Boolean) : UserManagementEvent
    data object OnPrintCouponDialogDismissed : UserManagementEvent
    data object OnPrintingReminderShouldBeDisplayed: UserManagementEvent
    data object OnPrintingDialogShouldBeDisplayed: UserManagementEvent


    /**
     *  Import Data
     */
    sealed interface ImportDataEvent : UserManagementEvent {
        data object OnClick : ImportDataEvent
        data class OnImportData(val stringUri: String) : ImportDataEvent
    }

    /**
     *  Filter Event
     */
    sealed interface FilterEvent : UserManagementEvent {
        data class OnClick(val newState: Boolean) : FilterEvent
        data class OnValueChanged(val newFilterType: FilterType) : FilterEvent
    }

    /**
     *  Notification Permission Dialog
     */
    sealed interface DialogPermissionEvent : UserManagementEvent {
        data object OnGrantPermission : DialogPermissionEvent
        data object OnContinueWithoutPermission : DialogPermissionEvent
        data object OnDismiss : DialogPermissionEvent
    }
}