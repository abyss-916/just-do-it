package com.escodro.repository

import com.escodro.domain.model.AppThemeOptions
import com.escodro.domain.model.LanguageOptions
import com.escodro.domain.usecase.preferences.PreferencesRepository
import com.escodro.repository.datasource.PreferencesDataSource
import com.escodro.repository.mapper.AppThemeOptionsMapper
import com.escodro.repository.mapper.LanguageOptionsMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class PreferencesRepositoryImpl(
    private val dataSource: PreferencesDataSource,
    private val themeMapper: AppThemeOptionsMapper,
    private val langMapper: LanguageOptionsMapper,
) : PreferencesRepository {

    override suspend fun updateAppTheme(theme: AppThemeOptions) {
        dataSource.updateAppTheme(themeMapper.toRepo(theme))
    }

    override fun loadAppTheme(): Flow<AppThemeOptions> =
        dataSource.loadAppTheme().map { themeMapper.toDomain(it) }

    override suspend fun updateLanguage(language: LanguageOptions) {
        dataSource.updateLanguage(langMapper.toRepo(language))
    }

    override fun loadLanguage(): Flow<LanguageOptions> =
        dataSource.loadLanguage().map { langMapper.toDomain(it) }
}
