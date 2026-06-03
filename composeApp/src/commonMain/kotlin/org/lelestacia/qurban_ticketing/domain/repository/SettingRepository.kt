package org.lelestacia.qurban_ticketing.domain.repository

import kotlinx.coroutines.flow.Flow
import org.lelestacia.qurban_ticketing.util.LanguageCode

interface SettingRepository {
    fun readPreferredLanguage(): Flow<String>
    suspend fun savePreferredLanguage(languageCode: LanguageCode)
}