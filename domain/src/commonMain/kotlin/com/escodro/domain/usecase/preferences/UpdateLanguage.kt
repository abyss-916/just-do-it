package com.escodro.domain.usecase.preferences

import com.escodro.domain.model.LanguageOptions

/**
 * Updates the current app language.
 */
class UpdateLanguage(private val preferencesRepository: PreferencesRepository) {

    suspend operator fun invoke(language: LanguageOptions) {
        preferencesRepository.updateLanguage(language)
    }
}
