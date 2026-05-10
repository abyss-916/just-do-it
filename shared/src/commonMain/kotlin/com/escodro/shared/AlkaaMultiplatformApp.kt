package com.escodro.shared

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.escodro.appstate.AlkaaAppState
import com.escodro.appstate.rememberAlkaaAppState
import com.escodro.designsystem.theme.AlkaaTheme
import com.escodro.home.presentation.Home
import com.escodro.shared.locale.setPlatformLocale
import com.escodro.shared.model.AppThemeOptions
import com.escodro.shared.model.LanguageOptions
import org.koin.compose.koinInject

/**
 * Root composable for the Alkaa multiplatform app.
 */
@Composable
fun AlkaaMultiplatformApp(
    modifier: Modifier = Modifier,
    appState: AlkaaAppState = rememberAlkaaAppState(),
) {
    val (isDarkTheme, languageTag) = rememberThemeAndLanguage()

    // Apply locale when language changes
    LaunchedEffect(languageTag) {
        setPlatformLocale(languageTag)
    }

    // Force recomposition when language changes by using a key
    LanguageKey(languageTag) {
        AlkaaTheme(isDarkTheme = isDarkTheme) {
            Home(
                appState = appState,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun rememberThemeAndLanguage(viewModel: AppViewModel = koinInject()): Pair<Boolean, String> {
    val isSystemDarkTheme = isSystemInDarkTheme()

    val theme by remember(viewModel) {
        viewModel.loadCurrentTheme()
    }.collectAsState(initial = AppThemeOptions.SYSTEM)

    val language by remember(viewModel) {
        viewModel.loadCurrentLanguage()
    }.collectAsState(initial = LanguageOptions.SYSTEM)

    val isDarkTheme = when (theme) {
        AppThemeOptions.LIGHT -> false
        AppThemeOptions.DARK -> true
        AppThemeOptions.SYSTEM -> isSystemDarkTheme
    }

    val localeTag = when (language) {
        LanguageOptions.SYSTEM -> ""
        LanguageOptions.ENGLISH -> "en"
        LanguageOptions.SPANISH -> "es"
        LanguageOptions.FRENCH -> "fr"
        LanguageOptions.PORTUGUESE -> "pt-BR"
        LanguageOptions.CHINESE -> "zh-CN"
    }

    return isDarkTheme to localeTag
}

@Composable
private fun LanguageKey(
    languageTag: String,
    content: @Composable () -> Unit,
) {
    var recomposeKey by remember { mutableIntStateOf(0) }

    LaunchedEffect(languageTag) {
        recomposeKey++
    }

    androidx.compose.runtime.key(recomposeKey) {
        content()
    }
}
