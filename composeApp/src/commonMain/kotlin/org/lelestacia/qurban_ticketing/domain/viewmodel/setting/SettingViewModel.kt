package org.lelestacia.qurban_ticketing.domain.viewmodel.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.lelestacia.qurban_ticketing.domain.repository.SettingRepository
import org.lelestacia.qurban_ticketing.domain.state_event.setting.SettingEvent
import org.lelestacia.qurban_ticketing.domain.state_event.setting.SettingState

class SettingViewModel(
    private val settingRepository: SettingRepository
) : ViewModel() {

    val state: StateFlow<SettingState>
        field = MutableStateFlow<SettingState>(SettingState())

    fun onEvent(event: SettingEvent) {
        when(event) {
            is SettingEvent.OnLanguageChanged -> viewModelScope.launch {
                settingRepository.savePreferredLanguage(languageCode = event.newLanguage.code)
            }
        }
    }
}