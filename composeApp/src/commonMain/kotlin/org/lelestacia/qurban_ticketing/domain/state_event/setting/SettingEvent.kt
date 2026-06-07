package org.lelestacia.qurban_ticketing.domain.state_event.setting

import org.lelestacia.qurban_ticketing.ui.dropdown.Language

sealed interface SettingEvent {
    data class OnLanguageChanged(val newLanguage: Language): SettingEvent
    data object OnExportDataClicked: SettingEvent
}