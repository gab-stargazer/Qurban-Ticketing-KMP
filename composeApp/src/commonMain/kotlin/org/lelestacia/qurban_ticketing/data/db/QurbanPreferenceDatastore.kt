package org.lelestacia.qurban_ticketing.data.db

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Storage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun createDataStore(storage: Storage<Preferences>): DataStore<Preferences> =
    DataStoreFactory.create(storage = storage)

const val dataStoreFileName = "qurban.preferences_pb"

class AppSettings(private val preference: DataStore<Preferences>) {

    private val languageKey =  stringPreferencesKey(LANGUAGE_KEY)
    private val recipientCouponKey = stringPreferencesKey(RECIPIENT_COUPON_KEY)
    private val participantCouponKey = stringPreferencesKey(PARTICIPANT_COUPON_KEY)

    fun readPreferredLanguage(): Flow<String> {
        return preference.data.map { preferences ->
            preferences[languageKey] ?: "id"
        }
    }

    suspend fun savePreferredLanguage(languageCode: String) {
        preference.updateData { preferences ->
            preferences.toMutablePreferences().also {
                it[languageKey] = languageCode
            }
        }
    }

    fun readRecipientCoupon(): Flow<String> {
        return preference.data.map { preferences ->
            preferences[recipientCouponKey] ?: ""
        }
    }

    suspend fun saveRecipientCoupon(url: String) {
        preference.updateData { preferences ->
            preferences.toMutablePreferences().also {
                it[recipientCouponKey] = url
            }
        }
    }

    fun readParticipantCoupon(): Flow<String> {
        return preference.data.map { preferences ->
            preferences[participantCouponKey] ?: ""
        }
    }

    suspend fun saveParticipantCoupon(url: String) {
        preference.updateData { preferences ->
            preferences.toMutablePreferences().also {
                it[participantCouponKey] = url
            }
        }
    }

    companion object {
        private const val LANGUAGE_KEY = "key_language"
        private const val RECIPIENT_COUPON_KEY = "recipient_coupon"
        private const val PARTICIPANT_COUPON_KEY = "participant_coupon"
    }
}