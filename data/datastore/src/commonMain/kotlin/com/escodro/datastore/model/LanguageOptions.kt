package com.escodro.datastore.model

/**
 * Enum to represent the app language selected by the user.
 *
 * @property id the language id
 * @property localeTag the locale tag (e.g. "en", "zh-CN")
 */
enum class LanguageOptions(val id: Int, val localeTag: String) {

    /**
     * System default language.
     */
    SYSTEM(id = 0, localeTag = ""),

    /**
     * English.
     */
    ENGLISH(id = 1, localeTag = "en"),

    /**
     * Spanish.
     */
    SPANISH(id = 2, localeTag = "es"),

    /**
     * French.
     */
    FRENCH(id = 3, localeTag = "fr"),

    /**
     * Portuguese (Brazil).
     */
    PORTUGUESE(id = 4, localeTag = "pt-BR"),

    /**
     * Chinese (Simplified).
     */
    CHINESE(id = 5, localeTag = "zh-CN"),
}
