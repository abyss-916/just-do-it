package com.escodro.shared.di

import com.escodro.shared.AppViewModel
import com.escodro.shared.mapper.AppThemeOptionsMapper
import com.escodro.shared.mapper.LanguageOptionsMapper
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    singleOf(::AppThemeOptionsMapper)
    factoryOf(::LanguageOptionsMapper)
    viewModelOf(::AppViewModel)
    includes(platformSharedModule)
}

/**
 * Provides the platform-specific dependencies.
 */
internal expect val platformSharedModule: Module
