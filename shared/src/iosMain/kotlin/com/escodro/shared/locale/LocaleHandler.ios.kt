package com.escodro.shared.locale

import platform.Foundation.NSUserDefaults

/**
 * iOS implementation: sets the AppleLocale user default.
 */
actual fun setPlatformLocale(localeTag: String) {
    if (localeTag.isEmpty()) {
        NSUserDefaults.standardUserDefaults.removeObjectForKey("AppleLocale")
        return
    }
    NSUserDefaults.standardUserDefaults.setObject(localeTag, "AppleLocale")
}
