package com.escodro.shared.locale

import java.util.Locale

/**
 * Desktop implementation: sets the default JVM locale.
 */
actual fun setPlatformLocale(localeTag: String) {
    if (localeTag.isEmpty()) {
        // Reset to system default
        return
    }
    val parts = localeTag.split("-")
    val locale = if (parts.size == 1) {
        Locale(parts[0])
    } else {
        Locale(parts[0], parts[1])
    }
    Locale.setDefault(locale)
}
