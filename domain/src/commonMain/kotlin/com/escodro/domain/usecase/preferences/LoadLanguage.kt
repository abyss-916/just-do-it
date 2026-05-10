package com.escodro.domain.usecase.preferences

import com.escodro.domain.model.LanguageOptions
import kotlinx.coroutines.flow.Flow

/**
 * Loads the current app language.
 */
class LoadLanguage(private val preferencesRepository: PreferencesRepository) {

    operator fun invoke(): Flow<LanguageOptions> =
        preferencesRepository.loadLanguage()
}
