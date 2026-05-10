package com.escodro.shared.mapper

import com.escodro.domain.model.LanguageOptions as DomainLangOptions
import com.escodro.shared.model.LanguageOptions as ViewDataLangOptions

/**
 * Maps LanguageOptions between Domain and shared ViewData.
 */
internal class LanguageOptionsMapper {

    fun toViewData(languageOptions: DomainLangOptions): ViewDataLangOptions =
        when (languageOptions) {
            DomainLangOptions.SYSTEM -> ViewDataLangOptions.SYSTEM
            DomainLangOptions.ENGLISH -> ViewDataLangOptions.ENGLISH
            DomainLangOptions.SPANISH -> ViewDataLangOptions.SPANISH
            DomainLangOptions.FRENCH -> ViewDataLangOptions.FRENCH
            DomainLangOptions.PORTUGUESE -> ViewDataLangOptions.PORTUGUESE
            DomainLangOptions.CHINESE -> ViewDataLangOptions.CHINESE
        }

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
