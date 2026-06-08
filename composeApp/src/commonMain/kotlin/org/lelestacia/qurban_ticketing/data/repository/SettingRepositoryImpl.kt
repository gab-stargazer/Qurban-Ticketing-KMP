package org.lelestacia.qurban_ticketing.data.repository

import kotlinx.coroutines.flow.Flow
import org.lelestacia.qurban_ticketing.data.db.AppSettings
import org.lelestacia.qurban_ticketing.domain.model.Status
import org.lelestacia.qurban_ticketing.domain.repository.SettingRepository
import org.lelestacia.qurban_ticketing.util.FileStorage
import org.lelestacia.qurban_ticketing.util.LanguageCode

class SettingRepositoryImpl(
    private val appSettings: AppSettings,
    private val fileStorage: FileStorage
) : SettingRepository {

    override fun readPreferredLanguage(): Flow<String> {
        return appSettings.readPreferredLanguage()
    }

    override suspend fun savePreferredLanguage(languageCode: LanguageCode) {
        appSettings.savePreferredLanguage(languageCode.code)
    }

    override fun readRecipientCustomCoupon(): Flow<String> {
        return appSettings.readRecipientCoupon()
    }

    override suspend fun saveRecipientCustomCoupon(url: String, byteArray: ByteArray) {
        if (url.isNotBlank()) {
            fileStorage.saveCoupon("${Status.Recipient.name}.png", byteArray)
        }
        appSettings.saveRecipientCoupon(url)
    }

    override fun readParticipantCustomCoupon(): Flow<String> {
        return appSettings.readParticipantCoupon()
    }

    override suspend fun saveParticipantCustomCoupon(url: String, byteArray: ByteArray) {
        if (url.isNotBlank()) {
            fileStorage.saveCoupon("${Status.Participant.name}.png", byteArray)
        }
        appSettings.saveParticipantCoupon(url)
    }
}