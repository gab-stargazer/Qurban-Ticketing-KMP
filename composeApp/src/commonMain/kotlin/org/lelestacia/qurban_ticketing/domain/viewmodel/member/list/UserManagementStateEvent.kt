package org.lelestacia.qurban_ticketing.domain.viewmodel.member.list

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import arrow.optics.optics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.lelestacia.qurban_ticketing.domain.model.User
import org.lelestacia.qurban_ticketing.ui.filter.FilterType
import org.lelestacia.qurban_ticketing.ui.filter.FilterType.All


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
    //  Create Coupon
    val isDialogPrintCouponShowed: Boolean = false,
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
    data object OnPrintCouponClicked : UserManagementEvent
    data object OnPrintCouponDialogDismissed : UserManagementEvent


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