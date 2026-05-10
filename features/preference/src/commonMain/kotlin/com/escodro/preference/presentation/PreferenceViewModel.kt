package com.escodro.preference.presentation

import androidx.lifecycle.ViewModel
import com.escodro.coroutines.AppCoroutineScope
import com.escodro.domain.usecase.preferences.LoadAppTheme
import com.escodro.domain.usecase.preferences.LoadLanguage
import com.escodro.domain.usecase.preferences.UpdateAppTheme
import com.escodro.domain.usecase.preferences.UpdateLanguage
import com.escodro.preference.mapper.AppThemeOptionsMapper
import com.escodro.preference.mapper.LanguageOptionsMapper
import com.escodro.preference.model.AppThemeOptions
import com.escodro.preference.model.LanguageOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class PreferenceViewModel(
    private val updateThemeUseCase: UpdateAppTheme,
    private val loadAppTheme: LoadAppTheme,
    private val updateLanguageUseCase: UpdateLanguage,
    private val loadLanguage: LoadLanguage,
    private val applicationScope: AppCoroutineScope,
    private val themeMapper: AppThemeOptionsMapper,
    private val langMapper: LanguageOptionsMapper,
) : ViewModel() {

    fun loadCurrentTheme(): Flow<AppThemeOptions> = loadAppTheme().map { themeMapper.toViewData(it) }

    fun updateTheme(theme: AppThemeOptions) = applicationScope.launch {
        val updatedTheme = themeMapper.toDomain(theme)
        updateThemeUseCase(updatedTheme)
    }

    fun loadCurrentLanguage(): Flow<LanguageOptions> =
        loadLanguage().map { langMapper.toViewData(it) }

    fun updateLanguage(language: LanguageOptions) = applicationScope.launch {
        val updatedLang = langMapper.toDomain(language)
        updateLanguageUseCase(updatedLang)
    }
}
