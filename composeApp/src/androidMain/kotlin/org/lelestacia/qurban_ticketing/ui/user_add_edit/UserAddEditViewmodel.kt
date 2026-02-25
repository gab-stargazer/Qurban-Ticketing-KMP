package org.lelestacia.qurban_ticketing.ui.user_add_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import org.lelestacia.qurban_ticketing.domain.model.User
import org.lelestacia.qurban_ticketing.domain.model.isParticipant
import org.lelestacia.qurban_ticketing.domain.repository.UserRepository
import org.lelestacia.qurban_ticketing.util.Navigator
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit.ScreenType
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit.ScreenType.EDIT
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UserAddEditViewmodel(
    private val screenType: ScreenType,
    private val initialData: User?,
    private val navigator: Navigator,
    private val repository: UserRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(
        UserAddEditState(
            screenType = screenType,
            name = initialData?.name.orEmpty(),
            address = initialData?.address.orEmpty()
        )
    )
    val state = _state.asStateFlow()

    fun onEvent(event: UserAddEditEvent) = viewModelScope.launch {
        when (event) {
            is UserAddEditEvent.OnNameChanged -> _state.update { currentState ->
                currentState.copy(
                    name = event.name,
                    nameError = null
                )
            }

            is UserAddEditEvent.OnAddressChanged -> _state.update { currentState ->
                currentState.copy(
                    address = event.newAddress
                )
            }


            is UserAddEditEvent.OnStatusChanged -> _state.update { currentState ->
                currentState.copy(
                    qurbanStatus = event.newStatus
                )
            }

            is UserAddEditEvent.OnTypeChanged -> _state.update { currentState ->
                currentState.copy(
                    qurbanType = event.newType
                )
            }


            UserAddEditEvent.OnBackPressed -> navigator.goBack()

            UserAddEditEvent.OnAddEditPressed -> insertOrEditParticipant()

            UserAddEditEvent.OnDeletePressed -> viewModelScope.launch {
                initialData?.let { member ->
                    _state.update { currentSate ->
                        currentSate.copy(
                            isLoading = true
                        )
                    }

                    repository.deleteMember(member)
                    navigator.goBack()
                }
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun insertOrEditParticipant() = viewModelScope.launch {

        val currentState = _state.updateAndGet { currentState ->
            currentState.copy(
                isLoading = true
            )
        }

        val nameError = currentState.validateName()

        val errors = listOf(
            nameError
        )

        if (errors.any { it.isLeft() }) {
            _state.update {
                it.copy(
                    nameError = nameError.leftOrNull(),
                    isLoading = false
                )
            }
            return@launch
        }

        when (screenType) {
            ScreenType.ADD -> {
                repository.insertUser(
                    User(
                        id = Uuid.generateV7(),
                        name = currentState.name,
                        address = currentState.address.ifBlank { null },
                        status = currentState.qurbanStatus,
                        type = if (currentState.qurbanStatus.isParticipant())
                            currentState.qurbanType
                        else null
                    )
                )
            }

            EDIT -> {
                repository.updateUser(
                    User(
                        id = initialData?.id
                            ?: throw Exception("ID is Null when trying to update user"),
                        name = currentState.name,
                        address = currentState.address.ifBlank { null },
                        status = currentState.qurbanStatus,
                        type =
                            if (currentState.qurbanStatus.isParticipant())
                                currentState.qurbanType
                            else null
                    )
                )
            }
        }

        navigator.goBack()
    }
}