package com.escodro.preference.model

import com.escodro.resources.Res
import com.escodro.resources.language_chinese
import com.escodro.resources.language_english
import com.escodro.resources.language_french
import com.escodro.resources.language_portuguese
import com.escodro.resources.language_spanish
import com.escodro.resources.language_system_default
import org.jetbrains.compose.resources.StringResource

/**
 * Enum to represent the app language selected by the user.
 *
 * @property id the language id
 * @property titleRes the string title resource
 */
enum class LanguageOptions(val id: Int, val titleRes: StringResource) {

    /**
     * System default language.
     */
    SYSTEM(id = 0, titleRes = Res.string.language_system_default),

    /**
     * English.
     */
    ENGLISH(id = 1, titleRes = Res.string.language_english),

    /**
     * Spanish.
     */
    SPANISH(id = 2, titleRes = Res.string.language_spanish),

    /**
     * French.
     */
    FRENCH(id = 3, titleRes = Res.string.language_french),

    /**
     * Portuguese (Brazil).
     */
    PORTUGUESE(id = 4, titleRes = Res.string.language_portuguese),

    /**
     * Chinese (Simplified).
     */
    CHINESE(id = 5, titleRes = Res.string.language_chinese),
}
