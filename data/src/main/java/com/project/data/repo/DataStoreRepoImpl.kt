package com.project.data.repo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.project.domain.repo.DataStoreRepoInterface
import kotlinx.coroutines.flow.first

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "TOKEN")

class DataStoreRepoImpl (
    private val context: Context
): DataStoreRepoInterface {
    override suspend fun saveToDataStore(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun readFromDataStore(key: String): String? {
        return try {
            val preferencesKey = stringPreferencesKey(key)
            val preferences = context.dataStore.data.first()
            preferences[preferencesKey]
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }
}