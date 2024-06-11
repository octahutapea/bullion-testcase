package com.bullion.bulliontestcase.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class UserPreferences(context: Context) {
    private val dataStore = context.dataStore

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
            preferences[ISLOGIN] = true
        }
    }

    fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN]
        }
    }

    fun getIsLogin(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[ISLOGIN] ?: false
        }
    }

    suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences[TOKEN] = ""
            preferences[ISLOGIN] = false
        }
    }

    companion object {
        private val TOKEN = stringPreferencesKey("token")
        private val ISLOGIN = booleanPreferencesKey("isLogin")
    }
}