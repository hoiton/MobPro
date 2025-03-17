package com.example.mydemo.business.user

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryPreferences(
    private val context: Context
) {
    companion object {
        private const val USER_PREFERENCES_NAME = "user_preferences"
        private val Context.dataStore by preferencesDataStore(
            name = USER_PREFERENCES_NAME
        )
    }

    val user: Flow<User> = context.dataStore.data.map { preferences ->
        val username = preferences[PreferencesKey.USER_NAME] ?: ""
        val age = preferences[PreferencesKey.USER_AGE] ?: -1
        val authorize = preferences[PreferencesKey.USER_AUTHORIZED] ?: false

        User(
            name = username,
            age = age,
            authorized = authorize
        )
    }

    suspend fun setUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.USER_NAME] = name
        }
    }

    suspend fun setUserAge(age: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.USER_AGE] = age
        }
    }

    suspend fun setUserAuthorize(authorize: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.USER_AUTHORIZED] = authorize
        }
    }

    object PreferencesKey {
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_AGE = intPreferencesKey("user_age")
        val USER_AUTHORIZED = booleanPreferencesKey("user_authorized")
    }
}