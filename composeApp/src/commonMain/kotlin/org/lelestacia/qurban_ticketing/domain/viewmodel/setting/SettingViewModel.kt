package org.lelestacia.qurban_ticketing.domain.viewmodel.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.lelestacia.qurban_ticketing.domain.background_scheduler.BackgroundScheduler
import org.lelestacia.qurban_ticketing.domain.repository.SettingRepository
import org.lelestacia.qurban_ticketing.domain.state_event.setting.SettingEvent
import org.lelestacia.qurban_ticketing.domain.state_event.setting.SettingState
import org.lelestacia.qurban_ticketing.ui.dropdown.Language
import org.lelestacia.qurban_ticketing.util.LanguageCode

class SettingViewModel(
    private val settingRepository: SettingRepository,
    private val importDataScheduler: BackgroundScheduler,
    private val exportDataScheduler: BackgroundScheduler
) : ViewModel() {

    private val _state = MutableStateFlow(SettingState())
    private val preferredLanguage = settingRepository.readPreferredLanguage()
    private val recipientCoupon = settingRepository.readRecipientCustomCoupon()
    private val participantCoupon = settingRepository.readParticipantCustomCoupon()

    val state: StateFlow<SettingState> = combine(
        flow = _state,
        flow2 = preferredLanguage,
        flow3 = recipientCoupon,
        flow4 = participantCoupon
    ) { setting, code, recipient, participant ->
        SettingState(
            preferredLanguage = Language.entries.first { it.code == LanguageCode(code) },
            recipientCustomCoupon = recipient,
            participantCustomCoupon = participant,
            isPermissionShownForImport = setting.isPermissionShownForImport,
            isPermissionShownForExport = setting.isPermissionShownForExport
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = SettingState()
    )

    fun onEvent(event: SettingEvent) {
        when (event) {
            is SettingEvent.OnLanguageChanged -> viewModelScope.launch {
                settingRepository.savePreferredLanguage(languageCode = event.newLanguage.code)
            }

            is SettingEvent.OnExportDataClicked -> {
                when (event.hasPermission) {
                    true -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isPermissionShownForExport = false
                            )
                        }
                        exportDataScheduler.execute()
                    }

                    false -> _state.update { currentState ->
                        currentState.copy(
                            isPermissionShownForExport = true
                        )
                    }
                }
            }

            is SettingEvent.OnImportDataClicked -> {
                when (event.hasPermission) {
                    true -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isPermissionShownForImport = false
                            )
                        }
                        exportDataScheduler.execute()
                    }

                    false -> _state.update { currentState ->
                        currentState.copy(
                            isPermissionShownForImport = true
                        )
                    }
                }
            }

            SettingEvent.OnDismissPermissionDialog -> _state.update { currentState ->
                currentState.copy(
                    isPermissionShownForExport = false,
                    isPermissionShownForImport = false
                )
            }

            is SettingEvent.OnImportData -> importDataScheduler.execute(event.stringUri)

            is SettingEvent.OnRecipientImageChanged -> viewModelScope.launch {
                settingRepository.saveRecipientCustomCoupon(event.uri, event.byteArray)
            }

            is SettingEvent.OnParticipantImageChanged -> viewModelScope.launch {
                settingRepository.saveParticipantCustomCoupon(event.uri, event.byteArray)
            }
        }
    }
}