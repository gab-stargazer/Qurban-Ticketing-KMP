package org.lelestacia.qurban_ticketing.domain.viewmodel.user_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.StringResource
import org.lelestacia.qurban_ticketing.domain.background_scheduler.BackgroundScheduler
import org.lelestacia.qurban_ticketing.domain.model.Status
import org.lelestacia.qurban_ticketing.domain.model.User
import org.lelestacia.qurban_ticketing.domain.repository.UserRepository
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.DialogPrintCouponEvent
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.DialogPrintCouponState
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.UserListEvent
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.UserListState
import org.lelestacia.qurban_ticketing.ui.dropdown.FilterType
import org.lelestacia.qurban_ticketing.util.toFormattedDate
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.dialog_print_coupon_error_date_cannot_be_empty
import qurbanticketing.composeapp.generated.resources.dialog_print_coupon_error_finish_time_cannot_be_emptu
import qurbanticketing.composeapp.generated.resources.dialog_print_coupon_error_location_cannot_be_empty
import qurbanticketing.composeapp.generated.resources.dialog_print_coupon_error_start_time_cannot_be_emptu

class UserListViewModel(
    private val userRepository: UserRepository,
    private val printCouponScheduler: BackgroundScheduler,
) : ViewModel() {

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    private val _filterType: MutableStateFlow<FilterType> = MutableStateFlow(FilterType.All)

    @OptIn(
        FlowPreview::class,
        ExperimentalCoroutinesApi::class
    )
    private val _users: Flow<PagingData<User>> = combine(
        _searchQuery,
        _filterType
    ) { searchQuery, filterType ->
        Pair(searchQuery, filterType)
    }.flatMapLatest { pair ->
        val searchQuery = pair.first
        when (pair.second) {
            FilterType.All -> {
                userRepository.getUsers(searchQuery)
            }

            FilterType.Participant -> {
                userRepository.getUsersByStatus(
                    name = searchQuery,
                    status = Status.Participant
                )
            }

            FilterType.Recipient -> {
                userRepository.getUsersByStatus(
                    name = searchQuery,
                    status = Status.Recipient
                )
            }
        }
    }

    private val _currentState: MutableStateFlow<UserListState> =
        MutableStateFlow(UserListState())

    val state = combine(
        flow = _searchQuery,
        flow2 = _filterType,
        flow3 = _currentState,
    ) { searchQuery, filterType, state ->
        UserListState(
            // Search and Filter
            searchQuery = searchQuery,
            filterType = filterType,

            //  Visibility State
            isFilterMenuOpened = state.isFilterMenuOpened,
            isFabMenuExpanded = state.isFabMenuExpanded,

            //  Permission
            isNotificationDialogForPrintCouponOpened = state.isNotificationDialogForPrintCouponOpened,

            //  Dialog Print Coupon
            isPrintingReminderOpened = state.isPrintingReminderOpened,
            isPrintingDialogOpened = state.isPrintingDialogOpened,
            dialogPrintCouponState = state.dialogPrintCouponState,

            users = _users,
            selectedUserIndex = state.selectedUserIndex,

            //  Trigger
            shouldLaunchExcelLauncher = state.shouldLaunchExcelLauncher
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = UserListState()
    )

    fun onEvent(event: UserListEvent) {
        when (event) {

            is UserListEvent.FilterEvent -> {
                when (event) {
                    is UserListEvent.FilterEvent.OnClick -> {
                        _currentState.update { currentState ->
                            currentState.copy(
                                isFilterMenuOpened = event.newState
                            )
                        }
                    }

                    is UserListEvent.FilterEvent.OnValueChanged -> {
                        _filterType.update { _ ->
                            event.newFilterType
                        }

                        _currentState.update { currentState ->
                            currentState.copy(
                                isFilterMenuOpened = false
                            )
                        }
                    }
                }
            }

            is UserListEvent.DialogPermissionEvent -> {
                when (event) {
                    UserListEvent.DialogPermissionEvent.OnGrantPermission -> {
                        onEvent(UserListEvent.DialogPermissionEvent.OnContinueWithoutPermission)
                    }

                    UserListEvent.DialogPermissionEvent.OnContinueWithoutPermission -> {
                        _currentState.update { currentState ->
                            currentState.copy(
                                isNotificationDialogForPrintCouponOpened = false,
                                isPrintingReminderOpened = true
                            )
                        }
                    }

                    UserListEvent.DialogPermissionEvent.OnDismiss -> {
                        _currentState.update { currentState ->
                            currentState.copy(
                                isNotificationDialogForPrintCouponOpened = false
                            )
                        }
                    }
                }
            }

            is UserListEvent.OnSearchQueryChanged -> _searchQuery.update {
                event.newSearchQuery
            }

            DialogPrintCouponEvent.OnSelectedPickupDateChanged -> _currentState.update { currentState ->
                currentState.copy(
                    dialogPrintCouponState = currentState.dialogPrintCouponState.copy(
                        datePickerStateError = null
                    )
                )
            }

            // UI Interaction
            is UserListEvent.OnFabMenuStateClicked -> _currentState.update { currentState ->
                currentState.copy(
                    isFabMenuExpanded = event.newFabMenuState
                )
            }


            is UserListEvent.OnPrintCouponClicked -> _currentState.update { currentState ->
                if (event.isNotificationPermissionNeeded) {
                    currentState.copy(
                        isFabMenuExpanded = false,
                        isNotificationDialogForPrintCouponOpened = true
                    )
                } else {
                    currentState.copy(
                        isFabMenuExpanded = false,
                        isPrintingReminderOpened = true
                    )
                }
            }

            //  Print Coupon Dialog
            UserListEvent.OnPrintingReminderShouldBeDisplayed -> _currentState.update { currentState ->
                currentState.copy(
                    isPrintingReminderOpened = true
                )
            }

            UserListEvent.OnPrintingDialogShouldBeDisplayed -> _currentState.update { currentState ->
                currentState.copy(
                    isPrintingReminderOpened = false,
                    isPrintingDialogOpened = true
                )
            }

            is DialogPrintCouponEvent.OnDatePicked -> {
                _currentState.value.dialogPrintCouponState.datePickerState.selectedDateMillis =
                    event.selectedDate
                _currentState.update { currentState ->
                    currentState.copy(
                        dialogPrintCouponState = currentState.dialogPrintCouponState.copy(
                            datePickerStateError = null
                        )
                    )
                }
            }

            is DialogPrintCouponEvent.OnStartTimePicked -> {
                _currentState.value.dialogPrintCouponState.startTime.hour = event.hour.value
                _currentState.value.dialogPrintCouponState.startTime.minute = event.minute.value
                _currentState.update { currentState ->
                    currentState.copy(
                        dialogPrintCouponState = currentState.dialogPrintCouponState.copy(
                            startTimeError = null
                        )
                    )
                }
            }

            is DialogPrintCouponEvent.OnFinishTimePicked -> {
                _currentState.value.dialogPrintCouponState.finishTime.hour = event.hour.value
                _currentState.value.dialogPrintCouponState.finishTime.minute = event.minute.value
                _currentState.update { currentState ->
                    currentState.copy(
                        dialogPrintCouponState = currentState.dialogPrintCouponState.copy(
                            finishTimeError = null
                        )
                    )
                }
            }

            UserListEvent.OnPrintCouponDialogDismissed -> _currentState.update { currentState ->
                currentState.copy(
                    isPrintingDialogOpened = false,
                    dialogPrintCouponState = DialogPrintCouponState()
                )
            }

            DialogPrintCouponEvent.OnPrintCouponConfirmed -> {
                val validationResult = validateDialogPrintCoupon()
                if (validationResult.locationError != null || validationResult.dateError != null) {
                    _currentState.update { currentState ->
                        currentState.copy(
                            dialogPrintCouponState = currentState.dialogPrintCouponState.copy(
                                locationError = validationResult.locationError,
                                datePickerStateError = validationResult.dateError,
                                startTimeError = validationResult.startTimeError,
                                finishTimeError = validationResult.finishTimeError
                            )
                        )
                    }
                    return
                }

                printCouponScheduler.execute(
                     _currentState.value.dialogPrintCouponState.location.text.toString(),
                     _currentState.value.dialogPrintCouponState.datePickerState.selectedDateMillis!!.toFormattedDate(),
                    _currentState.value.dialogPrintCouponState.startTime.hour,
                    _currentState.value.dialogPrintCouponState.startTime.minute,
                    _currentState.value.dialogPrintCouponState.finishTime.hour,
                    _currentState.value.dialogPrintCouponState.finishTime.minute,
                )

                _currentState.update { currentState ->
                    currentState.copy(
                        isPrintingDialogOpened = false,
                        dialogPrintCouponState = DialogPrintCouponState()
                    )
                }
            }
            //  End Dialog Print Coupon

            is UserListEvent.OnUserClicked -> _currentState.update { currentState ->
                currentState.copy(
                    selectedUserIndex = event.index
                )
            }
        }
    }

    private fun validateDialogPrintCoupon(): DialogPrintCouponValidationResult {
        val currentState = state.value
        val locationError = currentState
            .dialogPrintCouponState
            .location
            .text
            .toString()
            .isBlank()

        val dateError = currentState
            .dialogPrintCouponState
            .datePickerState
            .selectedDateMillis == null

        val startTimeError = currentState
            .dialogPrintCouponState
            .startTime
            .hour == 0

        val finishTimeError = currentState
            .dialogPrintCouponState
            .finishTime
            .hour == 0

        return DialogPrintCouponValidationResult(
            locationError =
                if (locationError) {
                    Res.string.dialog_print_coupon_error_location_cannot_be_empty
                } else null,
            dateError =
                if (dateError) {
                    Res.string.dialog_print_coupon_error_date_cannot_be_empty
                } else null,
            startTimeError =
                if (startTimeError) {
                    Res.string.dialog_print_coupon_error_start_time_cannot_be_emptu
                } else null,
            finishTimeError =
                if (finishTimeError) {
                    Res.string.dialog_print_coupon_error_finish_time_cannot_be_emptu
                } else null
        )
    }

    private data class DialogPrintCouponValidationResult(
        val locationError: StringResource?,
        val dateError: StringResource?,
        val startTimeError: StringResource?,
        val finishTimeError: StringResource?
    )
}