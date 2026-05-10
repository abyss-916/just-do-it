package com.escodro.repository.mapper

import com.escodro.domain.model.LanguageOptions as DomainLangOptions
import com.escodro.repository.model.LanguageOptions as RepoLangOptions

/**
 * Maps LanguageOptions between Repository and Domain.
 */
internal class LanguageOptionsMapper {

    /**
     * Maps LanguageOptions from Repo to Domain.
     */
    fun toDomain(languageOptions: RepoLangOptions): DomainLangOptions =
        when (languageOptions) {
            RepoLangOptions.SYSTEM -> DomainLangOptions.SYSTEM
            RepoLangOptions.ENGLISH -> DomainLangOptions.ENGLISH
            RepoLangOptions.SPANISH -> DomainLangOptions.SPANISH
            RepoLangOptions.FRENCH -> DomainLangOptions.FRENCH
            RepoLangOptions.PORTUGUESE -> DomainLangOptions.PORTUGUESE
            RepoLangOptions.CHINESE -> DomainLangOptions.CHINESE
        }

    /**
     * Maps LanguageOptions from Domain to Repo.
     */
    fun toRepo(languageOptions: DomainLangOptions): RepoLangOptions =
        when (languageOptions) {
            DomainLangOptions.SYSTEM -> RepoLangOptions.SYSTEM
            DomainLangOptions.ENGLISH -> RepoLangOptions.ENGLISH
            DomainLangOptions.SPANISH -> RepoLangOptions.SPANISH
            DomainLangOptions.FRENCH -> RepoLangOptions.FRENCH
            DomainLangOptions.PORTUGUESE -> RepoLangOptions.PORTUGUESE
            DomainLangOptions.CHINESE -> RepoLangOptions.CHINESE
        }
}
