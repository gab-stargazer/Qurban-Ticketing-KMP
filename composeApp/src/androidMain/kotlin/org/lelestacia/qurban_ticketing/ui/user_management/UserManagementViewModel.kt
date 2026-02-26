package org.lelestacia.qurban_ticketing.ui.user_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.lelestacia.qurban_ticketing.domain.background_scheduler.BackgroundScheduler
import org.lelestacia.qurban_ticketing.domain.model.Status.Participant
import org.lelestacia.qurban_ticketing.domain.model.Status.Recipient
import org.lelestacia.qurban_ticketing.domain.model.User
import org.lelestacia.qurban_ticketing.domain.repository.UserRepository
import org.lelestacia.qurban_ticketing.ui.filter.FilterType
import org.lelestacia.qurban_ticketing.util.toFormattedDate
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.dialog_print_coupon_error_date_cannot_be_empty
import qurbanticketing.composeapp.generated.resources.dialog_print_coupon_error_location_cannot_be_empty

class UserManagementViewModel(
    private val userRepository: UserRepository,
    private val importDataScheduler: BackgroundScheduler,
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
                    status = Participant
                )
            }

            FilterType.Recipient -> {
                userRepository.getUsersByStatus(
                    name = searchQuery,
                    status = Recipient
                )
            }
        }
    }

    private val _currentState: MutableStateFlow<UserManagementState> =
        MutableStateFlow(UserManagementState())

    val state = combine(
        flow = _searchQuery,
        flow2 = _filterType,
        flow3 = _currentState,
    ) { searchQuery, filterType, state ->
        UserManagementState(
            // Search and Filter
            searchQuery = searchQuery,
            filterType = filterType,

            //  Visibility State
            isFilterMenuOpened = state.isFilterMenuOpened,
            isFabMenuExpanded = state.isFabMenuExpanded,
            isNotificationPermissionDialogOpened = state.isNotificationPermissionDialogOpened,
            isNotificationDialogForImportDataOpened = state.isNotificationDialogForImportDataOpened,
            isNotificationDialogForPrintCouponOpened = state.isNotificationDialogForPrintCouponOpened,

            //  Dialog Print Coupon
            isDialogPrintCouponShowed = state.isDialogPrintCouponShowed,
            dialogPrintCouponState = state.dialogPrintCouponState,

            users = _users,
            selectedUserIndex = state.selectedUserIndex,

            //  Trigger
            shouldLaunchExcelLauncher = state.shouldLaunchExcelLauncher
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = UserManagementState()
    )

    fun onEvent(event: UserManagementEvent) {
        when (event) {

            //  Action after permission

            is UserManagementEvent.OnImportData -> {
                _currentState.update { currentState ->
                    currentState.copy(
                        shouldLaunchExcelLauncher = false
                    )
                }

                importDataScheduler.execute(event.uri.toString())
            }


            //  UI Event
            is UserManagementEvent.OnFilterTypeChanged -> _filterType.update {
                event.newFilterType
            }

            //  Dialog Permission
            UserManagementEvent.OnDialogPermissionDismissed -> _currentState.update { currentState ->
                currentState.copy(
                    isNotificationDialogForImportDataOpened = false,
                    isNotificationDialogForPrintCouponOpened = false
                )
            }

            UserManagementEvent.OnContinueWithoutPermission -> viewModelScope.launch {


                if (state.value.isNotificationDialogForImportDataOpened) {
                    _currentState.update { currentState ->
                        currentState.copy(
                            isNotificationDialogForImportDataOpened = false,
                            isNotificationDialogForPrintCouponOpened = false,
                            shouldLaunchExcelLauncher = true
                        )
                    }
                } else if (state.value.isNotificationDialogForPrintCouponOpened) {
                    _currentState.update { currentState ->
                        currentState.copy(
                            isNotificationDialogForImportDataOpened = false,
                            isNotificationDialogForPrintCouponOpened = false
                        )
                    }
                }
            }

            UserManagementEvent.OnDialogPermissionGranted -> {
                onEvent(UserManagementEvent.OnContinueWithoutPermission)
            }
            //  End Dialog Permission

            is UserManagementEvent.OnSearchQueryChanged -> _searchQuery.update {
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
            is UserManagementEvent.OnFabMenuStateClicked -> _currentState.update { currentState ->
                currentState.copy(
                    isFabMenuExpanded = event.newFabMenuState
                )
            }

            is UserManagementEvent.OnFilterMenuClicked -> _currentState.update { currentState ->
                currentState.copy(
                    isFilterMenuOpened = event.newFilterMenuState
                )
            }

            UserManagementEvent.OnImportDataClicked -> _currentState.update { currentState ->
                currentState.copy(
                    isFabMenuExpanded = false,
                    isNotificationDialogForImportDataOpened = true
                )
            }

            UserManagementEvent.OnPrintCouponClicked -> _currentState.update { currentState ->
                currentState.copy(
                    isFabMenuExpanded = false,
                    isDialogPrintCouponShowed = true
                )
            }

            //  Print Coupon Dialog
            is DialogPrintCouponEvent.OnLocationChanged -> _currentState.update { currentState ->
                currentState.copy(
                    dialogPrintCouponState = currentState.dialogPrintCouponState.copy(
                        location = event.newLocation,
                        locationError = null
                    )
                )
            }

            is DialogPrintCouponEvent.OnDatePicked -> {
                _currentState.value.dialogPrintCouponState.datePickerState.selectedDateMillis = event.selectedDate
                _currentState.update { currentState ->
                    currentState.copy(
                        dialogPrintCouponState = currentState.dialogPrintCouponState.copy(
                            datePickerStateError = null
                        )
                    )
                }
            }

            UserManagementEvent.OnPrintCouponDialogDismissed -> _currentState.update { currentState ->
                currentState.copy(
                    isDialogPrintCouponShowed = false
                )
            }

            DialogPrintCouponEvent.OnPrintCouponConfirmedWithPermission -> {
                val validationResult = validateDialogPrintCoupon()
                if (validationResult.locationError != null || validationResult.dateError != null) {
                    _currentState.update { currentState ->
                        currentState.copy(
                            dialogPrintCouponState = currentState.dialogPrintCouponState.copy(
                                locationError = validationResult.locationError,
                                datePickerStateError = validationResult.dateError
                            )
                        )
                    }
                    return
                }

                printCouponScheduler.execute(
                    state.value.dialogPrintCouponState.location,
                    (state.value.dialogPrintCouponState.datePickerState.selectedDateMillis ?: return).toFormattedDate()
                )

                _currentState.update { currentState ->
                    currentState.copy(
                        dialogPrintCouponState = DialogPrintCouponState(),
                        isDialogPrintCouponShowed = false
                    )
                }
            }

            DialogPrintCouponEvent.OnPrintCouponConfirmedWithoutPermission -> {
                val validationResult = validateDialogPrintCoupon()
                if (validationResult.locationError != null || validationResult.dateError != null) {
                    _currentState.update { currentState ->
                        currentState.copy(
                            dialogPrintCouponState = currentState.dialogPrintCouponState.copy(
                                locationError = validationResult.locationError,
                                datePickerStateError = validationResult.dateError
                            )
                        )
                    }
                    return
                }

                _currentState.update { currentState ->
                    currentState.copy(
                        isNotificationDialogForPrintCouponOpened = true
                    )
                }
            }
            //  End Dialog Print Coupon

            is UserManagementEvent.OnUserClicked -> _currentState.update { currentState ->
                currentState.copy(
                    selectedUserIndex = event.index
                )
            }
        }
    }

    private fun validateDialogPrintCoupon(): DialogPrintCouponValidationResult {
        val locationError = state.value.dialogPrintCouponState.location.isBlank()
        val dateError = state.value.dialogPrintCouponState.datePickerState.selectedDateMillis == null

        return DialogPrintCouponValidationResult(
            locationError =
                if (locationError) {
                    Res.string.dialog_print_coupon_error_location_cannot_be_empty
                } else null,
            dateError =
                if (dateError) {
                    Res.string.dialog_print_coupon_error_date_cannot_be_empty
                } else null
        )
    }

    private data class DialogPrintCouponValidationResult(
        val locationError: StringResource?,
        val dateError: StringResource?,
    )
}