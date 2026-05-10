package com.escodro.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.escodro.datastore.mapper.AppThemeOptionsMapper
import com.escodro.datastore.mapper.LanguageOptionsMapper
import com.escodro.repository.datasource.PreferencesDataSource
import com.escodro.repository.model.AppThemeOptions
import com.escodro.repository.model.LanguageOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.escodro.datastore.model.AppThemeOptions as DataStoreThemeOptions
import com.escodro.datastore.model.LanguageOptions as DataStoreLangOptions

internal class PreferencesDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
    private val themeMapper: AppThemeOptionsMapper,
    private val langMapper: LanguageOptionsMapper,
) : PreferencesDataSource {

    override suspend fun updateAppTheme(theme: AppThemeOptions) {
        dataStore.edit { settings ->
            settings[APP_THEME_OPTION] = themeMapper.toDataStore(theme).id
        }
    }

    override fun loadAppTheme(): Flow<AppThemeOptions> =
        dataStore.data.map { preferences ->
            val id = preferences[APP_THEME_OPTION] ?: DataStoreThemeOptions.SYSTEM.id
            val result =
                DataStoreThemeOptions.entries.find { it.id == id } ?: DataStoreThemeOptions.SYSTEM
            themeMapper.toRepo(result)
        }

    override suspend fun updateLanguage(language: LanguageOptions) {
        dataStore.edit { settings ->
            settings[LANGUAGE_OPTION] = langMapper.toDataStore(language).id
        }
    }

    override fun loadLanguage(): Flow<LanguageOptions> =
        dataStore.data.map { preferences ->
            val id = preferences[LANGUAGE_OPTION] ?: DataStoreLangOptions.SYSTEM.id
            val result =
                DataStoreLangOptions.entries.find { it.id == id } ?: DataStoreLangOptions.SYSTEM
            langMapper.toRepo(result)
        }

    private companion object {
        val APP_THEME_OPTION = intPreferencesKey("alkaa_theme_option")
        val LANGUAGE_OPTION = intPreferencesKey("alkaa_language_option")
    }
}
