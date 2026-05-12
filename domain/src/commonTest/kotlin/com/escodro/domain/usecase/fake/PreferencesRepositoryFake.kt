package com.escodro.domain.usecase.fake

import com.escodro.domain.model.AppThemeOptions
import com.escodro.domain.model.LanguageOptions
import com.escodro.domain.usecase.preferences.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class PreferencesRepositoryFake : PreferencesRepository {

    private var theme: AppThemeOptions = AppThemeOptions.SYSTEM

    private var language: LanguageOptions = LanguageOptions.SYSTEM

    override suspend fun updateAppTheme(theme: AppThemeOptions) {
        this.theme = theme
    }

    override fun loadAppTheme(): Flow<AppThemeOptions> =
        flowOf(theme)

    override suspend fun updateLanguage(language: LanguageOptions) {
        this.language = language
    }

    override fun loadLanguage(): Flow<LanguageOptions> =
        flowOf(language)
}
