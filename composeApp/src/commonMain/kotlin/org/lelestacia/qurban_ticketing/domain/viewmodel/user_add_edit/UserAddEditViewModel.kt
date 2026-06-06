package org.lelestacia.qurban_ticketing.domain.viewmodel.user_add_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.lelestacia.qurban_ticketing.domain.model.User
import org.lelestacia.qurban_ticketing.domain.repository.UserRepository
import org.lelestacia.qurban_ticketing.domain.state_event.user_add_edit.UserAddEditEvent
import org.lelestacia.qurban_ticketing.domain.state_event.user_add_edit.UserAddEditState
import org.lelestacia.qurban_ticketing.util.Navigator
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserAddEditViewModel(
    private val screenType: UserAddEdit.ScreenType,
    private val initialData: User?,
    private val navigator: Navigator,
    private val repository: UserRepository
) : ViewModel() {

    val state: StateFlow<UserAddEditState>
        field = MutableStateFlow(
            UserAddEditState(
                screenType = screenType,
                name = initialData?.name.orEmpty(),
                address = initialData?.address.orEmpty()
            )
        )

    fun onEvent(event: UserAddEditEvent) {
        when (event) {
            is UserAddEditEvent.OnNameChanged -> {
                state.update {
                    it.copy(
                        name = event.name,
                    )
                }
            }

            is UserAddEditEvent.OnAddressChanged -> {
                state.update {
                    it.copy(
                        address = event.address,
                    )
                }
            }

            is UserAddEditEvent.OnStatusChanged -> {
                state.update {
                    it.copy(
                        status = event.newQurbanStatus
                    )
                }
            }

            is UserAddEditEvent.OnTypeChanged -> {
                state.update {
                    it.copy(
                        type = event.newQurbanType,
                    )
                }
            }

            UserAddEditEvent.OnAddEditPressed -> {
                viewModelScope.launch {
                    when (state.value.screenType) {
                        UserAddEdit.ScreenType.ADD -> {
                            repository.insertUser(
                                User(
                                    id = Uuid.generateV7(),
                                    name = state.value.name,
                                    address = state.value.address.ifEmpty { null },
                                    status = state.value.status,
                                    type = state.value.type,
                                )
                            )
                        }

                        UserAddEdit.ScreenType.EDIT -> {
                            repository.updateUser(
                                (initialData ?: return@launch).copy(
                                    name = state.value.name,
                                    address = state.value.address.ifEmpty { null },
                                    status = state.value.status,
                                    type = state.value.type,
                                )
                            )
                        }
                    }

                    navigator.onBackPressed()
                }
            }

            UserAddEditEvent.OnBackPressed -> {
                navigator.onBackPressed()
            }

            UserAddEditEvent.OnDeletePressed -> {
                viewModelScope.launch {
                    repository.deleteMember(initialData as User)
                    navigator.onBackPressed()
                }
            }
        }
    }
}