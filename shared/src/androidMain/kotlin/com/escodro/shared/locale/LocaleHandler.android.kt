package com.escodro.shared.locale

import java.util.Locale

/**
 * Android implementation: sets the default JVM locale.
 */
actual fun setPlatformLocale(localeTag: String) {
    if (localeTag.isEmpty()) {
        return
    }
    val locale = Locale.forLanguageTag(localeTag)
    Locale.setDefault(locale)
}
