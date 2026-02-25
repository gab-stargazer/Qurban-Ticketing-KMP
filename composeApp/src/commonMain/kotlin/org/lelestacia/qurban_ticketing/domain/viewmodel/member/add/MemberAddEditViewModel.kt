package org.lelestacia.qurban_ticketing.domain.viewmodel.member.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.lelestacia.qurban_ticketing.domain.model.User
import org.lelestacia.qurban_ticketing.domain.repository.UserRepository
import org.lelestacia.qurban_ticketing.util.Navigator
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit.ScreenType
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit.ScreenType.ADD
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit.ScreenType.EDIT
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class MemberAddEditViewModel(
    private val screenType: ScreenType,
    private val initialData: User?,
    private val navigator: Navigator,
    private val repository: UserRepository
) : ViewModel() {

    val state: StateFlow<MemberAddEditState>
        field = MutableStateFlow(
            MemberAddEditState(
                screenType = screenType,
                name = initialData?.name.orEmpty(),
                address = initialData?.address.orEmpty()
            )
        )

    fun onEvent(event: MemberAddEditEvent) {
        when (event) {
            is MemberAddEditEvent.OnNameChanged -> {
                state.update {
                    it.copy(
                        name = event.name,
                    )
                }
            }

            is MemberAddEditEvent.OnAddressChanged -> {
                state.update {
                    it.copy(
                        address = event.address,
                    )
                }
            }

            is MemberAddEditEvent.OnStatusChanged -> {
                state.update {
                    it.copy(
                        status = event.newQurbanStatus
                    )
                }
            }

            is MemberAddEditEvent.OnTypeChanged -> {
                state.update {
                    it.copy(
                        type = event.newQurbanType,
                    )
                }
            }

            MemberAddEditEvent.OnAddEditPressed -> {
                viewModelScope.launch {
                    when (state.value.screenType) {
                        ADD -> {
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

                        EDIT -> {
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

                    navigator.goBack()
                }
            }

            MemberAddEditEvent.OnBackPressed -> {
                navigator.goBack()
            }

            MemberAddEditEvent.OnDeletePressed -> {
                viewModelScope.launch {
                    repository.deleteMember(initialData as User)
                    navigator.goBack()
                }
            }
        }
    }
}