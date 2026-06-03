package org.lelestacia.qurban_ticketing.data.repository

import kotlinx.coroutines.flow.Flow
import org.lelestacia.qurban_ticketing.data.db.AppSettings
import org.lelestacia.qurban_ticketing.domain.repository.SettingRepository
import org.lelestacia.qurban_ticketing.util.LanguageCode

class SettingRepositoryImpl(
    private val appSettings: AppSettings
): SettingRepository {

    override fun readPreferredLanguage(): Flow<String> {
        return appSettings.readPreferredLanguage()
    }

    override suspend fun savePreferredLanguage(languageCode: LanguageCode) {
        appSettings.savePreferredLanguage(languageCode.code)
    }
}