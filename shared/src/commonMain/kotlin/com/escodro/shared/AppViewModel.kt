package com.escodro.shared

import androidx.lifecycle.ViewModel
import com.escodro.domain.usecase.preferences.LoadAppTheme
import com.escodro.domain.usecase.preferences.LoadLanguage
import com.escodro.shared.mapper.AppThemeOptionsMapper
import com.escodro.shared.mapper.LanguageOptionsMapper
import com.escodro.shared.model.AppThemeOptions
import com.escodro.shared.model.LanguageOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AppViewModel(
    private val loadAppTheme: LoadAppTheme,
    private val loadLanguage: LoadLanguage,
    private val themeMapper: AppThemeOptionsMapper,
    private val langMapper: LanguageOptionsMapper,
) : ViewModel() {

    fun loadCurrentTheme(): Flow<AppThemeOptions> = loadAppTheme().map { themeMapper.toViewData(it) }

    fun loadCurrentLanguage(): Flow<LanguageOptions> =
        loadLanguage().map { langMapper.toViewData(it) }
}
