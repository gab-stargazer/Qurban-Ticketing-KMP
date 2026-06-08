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
data class UserListState(
    val searchQuery: String = "",
    val filterType: FilterType = All,
    val isFilterMenuOpened: Boolean = false,
    val isFabMenuExpanded: Boolean = false,

    //  Permission Dialog
    val isNotificationDialogForPrintCouponOpened: Boolean = false,

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

sealed interface UserListEvent {
    data class OnSearchQueryChanged(
        val newSearchQuery: String,
    ) : UserListEvent

    data class OnUserClicked(
        val index: Int?,
    ) : UserListEvent

    //  UI State Event
    data class OnFabMenuStateClicked(
        val newFabMenuState: Boolean
    ) : UserListEvent


    //  Print Coupon
    data class OnPrintCouponClicked(val isNotificationPermissionNeeded: Boolean) : UserListEvent
    data object OnPrintCouponDialogDismissed : UserListEvent
    data object OnPrintingReminderShouldBeDisplayed: UserListEvent
    data object OnPrintingDialogShouldBeDisplayed: UserListEvent

    /**
     *  Filter Event
     */
    sealed interface FilterEvent : UserListEvent {
        data class OnClick(val newState: Boolean) : FilterEvent
        data class OnValueChanged(val newFilterType: FilterType) : FilterEvent
    }

    /**
     *  Notification Permission Dialog
     */
    sealed interface DialogPermissionEvent : UserListEvent {
        data object OnGrantPermission : DialogPermissionEvent
        data object OnContinueWithoutPermission : DialogPermissionEvent
        data object OnDismiss : DialogPermissionEvent
    }
}