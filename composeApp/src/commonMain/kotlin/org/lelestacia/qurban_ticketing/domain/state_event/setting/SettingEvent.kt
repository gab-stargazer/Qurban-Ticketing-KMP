package org.lelestacia.qurban_ticketing.domain.state_event.setting

import org.lelestacia.qurban_ticketing.ui.dropdown.Language

sealed interface SettingEvent {
    data class OnLanguageChanged(val newLanguage: Language) : SettingEvent
    data class OnExportDataClicked(val hasPermission: Boolean) : SettingEvent
    data class OnImportDataClicked(val hasPermission: Boolean) : SettingEvent
    data class OnRecipientImageChanged(val uri: String, val byteArray: ByteArray) : SettingEvent
    data class OnParticipantImageChanged(val uri: String, val byteArray: ByteArray) : SettingEvent
    data class OnImportData(val stringUri: String) : SettingEvent
    data object OnDismissPermissionDialog : SettingEvent
}