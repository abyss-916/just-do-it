package com.escodro.category.presentation

import androidx.compose.runtime.Composable
import com.escodro.resources.Res
import com.escodro.resources.category_default_personal
import com.escodro.resources.category_default_shopping
import com.escodro.resources.category_default_work
import org.jetbrains.compose.resources.stringResource

/**
 * Maps English default category names to localized display strings.
 * User-created categories (non-matching names) are returned as-is.
 */
@Composable
fun LocalizedCategoryName(storedName: String): String =
    when (storedName) {
        "Personal" -> stringResource(Res.string.category_default_personal)
        "Work" -> stringResource(Res.string.category_default_work)
        "Shopping List" -> stringResource(Res.string.category_default_shopping)
        else -> storedName
    }

/**
 * Reverses localized display names back to their English database keys.
 * Custom names that don't match any known localization are returned as-is.
 */
fun toStoredCategoryName(displayName: String): String =
    when (displayName) {
        "个人", "Personal", "Personnel" -> "Personal"
        "工作", "Work", "Trabajo", "Travail" -> "Work"
        "购物清单", "Shopping List", "Lista de compras" -> "Shopping List"
        else -> displayName
    }
