package com.escodro.preference.mapper

import com.escodro.domain.model.LanguageOptions as DomainLangOptions
import com.escodro.preference.model.LanguageOptions as ViewDataLangOptions

/**
 * Maps LanguageOptions between Domain and ViewData.
 */
internal class LanguageOptionsMapper {

    /**
     * Maps LanguageOptions from Domain to ViewData.
     */
    fun toViewData(languageOptions: DomainLangOptions): ViewDataLangOptions =
        when (languageOptions) {
            DomainLangOptions.SYSTEM -> ViewDataLangOptions.SYSTEM
            DomainLangOptions.ENGLISH -> ViewDataLangOptions.ENGLISH
            DomainLangOptions.SPANISH -> ViewDataLangOptions.SPANISH
            DomainLangOptions.FRENCH -> ViewDataLangOptions.FRENCH
            DomainLangOptions.PORTUGUESE -> ViewDataLangOptions.PORTUGUESE
            DomainLangOptions.CHINESE -> ViewDataLangOptions.CHINESE
        }

    /**
     * Maps LanguageOptions from ViewData to Domain.
     */
    fun toDomain(languageOptions: ViewDataLangOptions): DomainLangOptions =
        when (languageOptions) {
            ViewDataLangOptions.SYSTEM -> DomainLangOptions.SYSTEM
            ViewDataLangOptions.ENGLISH -> DomainLangOptions.ENGLISH
            ViewDataLangOptions.SPANISH -> DomainLangOptions.SPANISH
            ViewDataLangOptions.FRENCH -> DomainLangOptions.FRENCH
            ViewDataLangOptions.PORTUGUESE -> DomainLangOptions.PORTUGUESE
            ViewDataLangOptions.CHINESE -> DomainLangOptions.CHINESE
        }
}
