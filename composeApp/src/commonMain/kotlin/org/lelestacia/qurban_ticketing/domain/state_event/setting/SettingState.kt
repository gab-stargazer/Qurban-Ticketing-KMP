package org.lelestacia.qurban_ticketing.domain.state_event.setting

import org.lelestacia.qurban_ticketing.ui.dropdown.Language

data class SettingState(
    val preferredLanguage: Language = Language.ID
)