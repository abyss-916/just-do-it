package com.escodro.datastore.mapper

import com.escodro.datastore.model.LanguageOptions as DataStoreLangOptions
import com.escodro.repository.model.LanguageOptions as RepoLangOptions

/**
 * Maps LanguageOptions between Repository and DataStore.
 */
internal class LanguageOptionsMapper {

    /**
     * Maps LanguageOptions from Repo to DataStore.
     */
    fun toDataStore(languageOptions: RepoLangOptions): DataStoreLangOptions =
        when (languageOptions) {
            RepoLangOptions.SYSTEM -> DataStoreLangOptions.SYSTEM
            RepoLangOptions.ENGLISH -> DataStoreLangOptions.ENGLISH
            RepoLangOptions.SPANISH -> DataStoreLangOptions.SPANISH
            RepoLangOptions.FRENCH -> DataStoreLangOptions.FRENCH
            RepoLangOptions.PORTUGUESE -> DataStoreLangOptions.PORTUGUESE
            RepoLangOptions.CHINESE -> DataStoreLangOptions.CHINESE
        }

    /**
     * Maps LanguageOptions from DataStore to Repo.
     */
    fun toRepo(languageOptions: DataStoreLangOptions): RepoLangOptions =
        when (languageOptions) {
            DataStoreLangOptions.SYSTEM -> RepoLangOptions.SYSTEM
            DataStoreLangOptions.ENGLISH -> RepoLangOptions.ENGLISH
            DataStoreLangOptions.SPANISH -> RepoLangOptions.SPANISH
            DataStoreLangOptions.FRENCH -> RepoLangOptions.FRENCH
            DataStoreLangOptions.PORTUGUESE -> RepoLangOptions.PORTUGUESE
            DataStoreLangOptions.CHINESE -> RepoLangOptions.CHINESE
        }
}
