package org.lelestacia.qurban_ticketing.domain.viewmodel.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.lelestacia.qurban_ticketing.domain.background_scheduler.BackgroundScheduler
import org.lelestacia.qurban_ticketing.domain.repository.SettingRepository
import org.lelestacia.qurban_ticketing.domain.state_event.setting.SettingEvent
import org.lelestacia.qurban_ticketing.domain.state_event.setting.SettingState

class SettingViewModel(
    private val settingRepository: SettingRepository,
    private val importDataScheduler: BackgroundScheduler,
    private val exportDataScheduler: BackgroundScheduler
) : ViewModel() {

    val state: StateFlow<SettingState>
        field = MutableStateFlow<SettingState>(SettingState())

    fun onEvent(event: SettingEvent) {
        when (event) {
            is SettingEvent.OnLanguageChanged -> viewModelScope.launch {
                settingRepository.savePreferredLanguage(languageCode = event.newLanguage.code)
            }

            is SettingEvent.OnExportDataClicked -> {
                when (event.hasPermission) {
                    true -> {
                        state.update { currentState ->
                            currentState.copy(
                                isPermissionShownForExport = false
                            )
                        }
                        exportDataScheduler.execute()
                    }

                    false -> state.update { currentState ->
                        currentState.copy(
                            isPermissionShownForExport = true
                        )
                    }
                }
            }

            is SettingEvent.OnImportDataClicked -> {
                when (event.hasPermission) {
                    true -> {
                        state.update { currentState ->
                            currentState.copy(
                                isPermissionShownForImport = false
                            )
                        }
                        exportDataScheduler.execute()
                    }

                    false -> state.update { currentState ->
                        currentState.copy(
                            isPermissionShownForImport = true
                        )
                    }
                }
            }

            SettingEvent.OnDismissPermissionDialog -> state.update { currentState ->
                currentState.copy(
                    isPermissionShownForExport = false,
                    isPermissionShownForImport = false
                )
            }

            is SettingEvent.OnImportData -> importDataScheduler.execute(event.stringUri)
        }
    }
}